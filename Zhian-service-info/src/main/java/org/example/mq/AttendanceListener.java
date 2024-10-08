package org.example.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.example.api.DeviceApi;
import org.example.api.PushApi;
import org.example.config.RabbitmqNameConfig;
import org.example.dao.AttendanceMapper;
import org.example.dao.EmployeeMapper;
import org.example.dao.NowPositionMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.example.entity.NowPosition;
import org.example.entity.Attendance;

import java.sql.Timestamp;
import java.util.Map;

@Component
public class AttendanceListener extends RabbitmqNameConfig {

    private static final String ID = "id";
    private static final String FINGER = "fingerid";

    @Autowired
    DeviceApi deviceApi;

    @Autowired
    AttendanceMapper attendanceMapper;

    @Autowired
    NowPositionMapper nowPositionMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    PushApi pushApi;

    @RabbitListener(queues = {ATD_QUEUE})
    public void AttendanceProcess(Message message, Channel channel) {
        String jsonString = new String(message.getBody());
        Map<String, String> dataMap = JSON.parseObject(jsonString, Map.class);
        String workshop = deviceApi.getWorkshop(dataMap.get("dev_id")).getData();
        long at = Integer.parseInt(dataMap.get("at"));
        String cardId = dataMap.get("value");
        if(dataMap.get("ds_id").equals(FINGER)){
            cardId = employeeMapper.getCardIdById(Integer.parseInt(dataMap.get("value")));
        }
        AttendanceUpdate(at,workshop,cardId);
    }


    public void AttendanceUpdate(long at,String workshop,String cardId) {
        //判断是否给前端推送位置信息
        boolean isSend = true;
        Timestamp timestamp = new Timestamp(at);
        int attendanceNums = attendanceMapper.getAttendanceByCardIdAndTime(cardId,timestamp);
        if(attendanceNums==0){
            attendanceMapper.insertAttendance(cardId,timestamp,1,null);
        }
        NowPosition nowPosition = nowPositionMapper.getNowPositionByCardId(cardId);

        /**
         * 生成要发给前端的信息体
         * 获取设备id对应的厂房号
         */
        NowPosition sendMessage = new NowPosition(cardId,timestamp, workshop);

        if(nowPosition==null){
            nowPositionMapper.insertNowPosition(cardId,timestamp, workshop);
        }
        else{
            if(nowPosition.getWorkshop().equals(workshop) && timestamp.getTime()-nowPosition.getTimestamp().getTime()>10000){
                nowPosition.setWorkshop("0");
                nowPositionMapper.updateNowPosition(cardId,nowPosition.getWorkshop(),timestamp);

                //设置前端的消息
                sendMessage.setWorkshop("0");

                Attendance attendance = attendanceMapper.getExitByCardIdAndTime(cardId,timestamp);
                if(attendance==null){
                    attendanceMapper.insertAttendance(cardId,timestamp,2,null);
                }
                else{
                    attendanceMapper.UpdateExitTimeByCardIdAndTime(cardId,timestamp);
                }

            }
            else if(timestamp.getTime()-nowPosition.getTimestamp().getTime()>10000) {
                nowPositionMapper.updateNowPosition(cardId,workshop,timestamp);
            }
            else{
                isSend = false;
            }
        }

        if(isSend){
            pushApi.attendancePush(sendMessage);
        }
    }
}
