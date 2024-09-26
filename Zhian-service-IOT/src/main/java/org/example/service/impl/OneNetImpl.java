package org.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.config.RabbitmqConfig;
import org.example.entity.OldOnenetMsg;
import org.example.entity.OnenetData;
import org.example.service.OneNetService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class OneNetImpl implements OneNetService {

    private static final String ID = "id";
    private static final String FINGER = "fingerid";
    private static final String TEMPERATURE = "Temp";
    private static final String HUMIDITY = "Humi";

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public String getAttendanceRecord(OnenetData data) {

        OldOnenetMsg oldOnenetMsg = data.getMsg();
        if(oldOnenetMsg.getType()==1){
            if(oldOnenetMsg.getDs_id().equals(ID) || oldOnenetMsg.getDs_id().equals(FINGER)){
                AttendanceHandler(oldOnenetMsg);
            } else if(oldOnenetMsg.getDs_id().equals(TEMPERATURE)){
                TemperatureHandler(oldOnenetMsg);
            } else if(oldOnenetMsg.getDs_id().equals(HUMIDITY)){
                HumidityHandler(oldOnenetMsg);
            }
        }
        else if(oldOnenetMsg.getType()==2){
            DeviceHandler(oldOnenetMsg);
        }
        return "over";
    }

    @Override
    public String getTemperatureValidate(String msg, String nonce, String signature) {
        return msg;
    }

    @Override
    public String getHumidityValidate(String msg, String nonce, String signature) {
        return msg;
    }

    @Override
    public String getAttendanceRecordValidate(String msg, String nonce, String signature) {
        return msg;
    }


    @Override
    public void AttendanceUpdate(OldOnenetMsg oldOnenetMsg) {
//        //判断是否给前端推送位置信息
//        boolean isSend = true;
//        Timestamp timestamp = new Timestamp(oldOnenetMsg.getAt());
//        int attendanceNums = attendanceMapper.getAttendanceByCardIdAndTime(oldOnenetMsg.getValue(),timestamp);
//        if(attendanceNums==0){
//            attendanceMapper.insertAttendance(oldOnenetMsg.getValue(),timestamp,1,null);
//        }
//        NowPosition nowPosition = nowPositionMapper.getNowPositionByCardId(oldOnenetMsg.getValue());
//
//        /**
//         * 生成要发给前端的信息体
//         * 获取设备id对应的厂房号
//         */
//        String workshop = deviceMapper.getWorkshopByDevice_id(Integer.toString(oldOnenetMsg.getDev_id()));
//        NowPosition sendMessage = new NowPosition(oldOnenetMsg.getValue(),timestamp, workshop);
//
//        if(nowPosition==null){
//            nowPositionMapper.insertNowPosition(oldOnenetMsg.getValue(),timestamp, workshop);
//        }
//        else{
//            if(nowPosition.getWorkshop().equals(workshop) && timestamp.getTime()-nowPosition.getTimestamp().getTime()>10000){
//                nowPosition.setWorkshop("0");
//                nowPositionMapper.updateNowPosition(oldOnenetMsg.getValue(),nowPosition.getWorkshop(),timestamp);
//
//                //设置前端的消息
//                sendMessage.setWorkshop("0");
//
//                Attendance attendance = attendanceMapper.getExitByCardIdAndTime(oldOnenetMsg.getValue(),timestamp);
//                if(attendance==null){
//                    attendanceMapper.insertAttendance(oldOnenetMsg.getValue(),timestamp,2,null);
//                }
//                else{
//                    attendanceMapper.UpdateExitTimeByCardIdAndTime(oldOnenetMsg.getValue(),timestamp);
//                }
//
//            }
//            else if(timestamp.getTime()-nowPosition.getTimestamp().getTime()>10000) {
//                nowPositionMapper.updateNowPosition(oldOnenetMsg.getValue(),workshop,timestamp);
//            }
//            else{
//                isSend = false;
//            }
//        }
//
//        if(isSend){
//            WebSocketMessage webSocketMessage = new WebSocketMessage(1,sendMessage);
//
//            //发送信息的代码块
//            Set<String> keys = LogEndpoint.onlineUsers.keySet();
//            for (String key : keys) {
//                try {
//                    LogEndpoint.onlineUsers.get(key).getBasicRemote().sendText(JSON.toJSONString(webSocketMessage)); //转换为json
//                }catch (Exception e){
//
//                }
//            }
//        }
    }

    private void TemperatureHandler(OldOnenetMsg oldOnenetMsg){
//        //更新温度
//        String workshop = deviceMapper.getWorkshopByDevice_id(Integer.toString(oldOnenetMsg.getDev_id()));
//        if(nowTemperatureMapper.getTemperatureNum(workshop)<1){
//            nowTemperatureMapper.insertTemperature(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }else {
//            nowTemperatureMapper.updateTemperature(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }
//        if(oldOnenetMsg.getAt()-temperaturesMapper.getTemperatureLastTime(workshop)>2*1000){
//            temperaturesMapper.insertTemperature(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }
        Map<String, Object> map = new HashMap<>();
        map.put("dev_id", oldOnenetMsg.getDev_id());
        map.put("value", oldOnenetMsg.getValue());
        map.put("at", oldOnenetMsg.getAt());
        rabbitTemplate.convertAndSend(RabbitmqConfig.TEMP_ROUTING_KEY, JSON.toJSONString(map));
    }

    private void HumidityHandler(OldOnenetMsg oldOnenetMsg){
//        //更新湿度
//        String workshop = deviceMapper.getWorkshopByDevice_id(Integer.toString(oldOnenetMsg.getDev_id()));
//        if(nowHumidityMapper.getHumidityNum(workshop)<1){
//            nowHumidityMapper.insertHumidity(workshop,oldOnenetMsg.getAt(),Float.parseFloat(oldOnenetMsg.getValue()));
//        }else {
//            nowHumidityMapper.updateHumidity(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }
//        if(oldOnenetMsg.getAt()-humiditysMapper.getHumiLastTime(workshop)>2*1000){
//            humiditysMapper.insertHumidity(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }
        Map<String, Object> map = new HashMap<>();
        map.put("dev_id", oldOnenetMsg.getDev_id());
        map.put("value", oldOnenetMsg.getValue());
        map.put("at", oldOnenetMsg.getAt());
        rabbitTemplate.convertAndSend(RabbitmqConfig.HUM_ROUTING_KEY, JSON.toJSONString(map));
    }

    private void AttendanceHandler(OldOnenetMsg oldOnenetMsg){
//        if(oldOnenetMsg.getDs_id().equals(FINGER)){
//            String cardId = employeeMapper.getCardIdById(Integer.parseInt(oldOnenetMsg.getValue()));
//            oldOnenetMsg.setValue(cardId);
//        }
//        AttendanceUpdate(oldOnenetMsg);
        Map<String, Object> map = new HashMap<>();
        map.put("dev_id", oldOnenetMsg.getDev_id());
        map.put("ds_id", oldOnenetMsg.getDs_id());
        map.put("value", oldOnenetMsg.getValue());
        map.put("at", oldOnenetMsg.getAt());
        rabbitTemplate.convertAndSend(RabbitmqConfig.ATD_ROUTING_KEY, JSON.toJSONString(map));
    }

    private void DeviceHandler(OldOnenetMsg oldOnenetMsg){
        if(oldOnenetMsg.getStatus()==1){
            Map<String, Object> map = new HashMap<>();
            map.put("dev_id", oldOnenetMsg.getDev_id());
            map.put("at", oldOnenetMsg.getAt());
            rabbitTemplate.convertAndSend(RabbitmqConfig.ONLINE_ROUTING_KEY, JSON.toJSONString(map));
        }
    }
}

