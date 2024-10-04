package org.example.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.example.api.DeviceApi;
import org.example.config.RabbitmqConfig;
import org.example.mapper.HumiditysMapper;
import org.example.mapper.TemperaturesMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OneNetListener extends RabbitmqConfig {

    @Autowired
    DeviceApi deviceApi;

    @Autowired
    TemperaturesMapper temperaturesMapper;

    @Autowired
    HumiditysMapper humiditysMapper;


    @RabbitListener(queues = {TEMP_QUEUE})
    public void TemperatureProcess(Message message, Channel channel) {
        String jsonString = new String(message.getBody());
        Map<String, String> dataMap = JSON.parseObject(jsonString, Map.class);
        String workshop = deviceApi.getWorkshop(dataMap.get("dev_id")).getData();
        long at = Integer.parseInt(dataMap.get("at"));


        if (at - temperaturesMapper.getTemperatureLastTime(workshop) > 2 * 1000) {
            temperaturesMapper.insertTemperature(workshop, Float.parseFloat(dataMap.get("value")), at);
//        }
//        /**更新redis**/

            //更新温度
//        String workshop = deviceMapper.getWorkshopByDevice_id(Integer.toString(oldOnenetMsg.getDev_id()));
//        if(nowTemperatureMapper.getTemperatureNum(workshop)<1){
//            nowTemperatureMapper.insertTemperature(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }else {
//            nowTemperatureMapper.updateTemperature(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }
//        if(oldOnenetMsg.getAt()-temperaturesMapper.getTemperatureLastTime(workshop)>2*1000){
//            temperaturesMapper.insertTemperature(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }

        }

    }

    @RabbitListener(queues = {HUM_QUEUE})
    public void HumidityProcess(Message message, Channel channel) {
        String jsonString = new String(message.getBody());
        Map<String, String> dataMap = JSON.parseObject(jsonString, Map.class);
        String workshop = deviceApi.getWorkshop(dataMap.get("dev_id")).getData();
        long at = Integer.parseInt(dataMap.get("at"));

        if(at-humiditysMapper.getHumiLastTime(workshop)>2*1000) {
            humiditysMapper.insertHumidity(workshop, Float.parseFloat(dataMap.get("value")), at);

            //更新湿度
//        String workshop = deviceMapper.getWorkshopByDevice_id(Integer.toString(oldOnenetMsg.getDev_id()));
//        if(nowHumidityMapper.getHumidityNum(workshop)<1){
//            nowHumidityMapper.insertHumidity(workshop,oldOnenetMsg.getAt(),Float.parseFloat(oldOnenetMsg.getValue()));
//        }else {
//            nowHumidityMapper.updateHumidity(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }
//        if(oldOnenetMsg.getAt()-humiditysMapper.getHumiLastTime(workshop)>2*1000){
//            humiditysMapper.insertHumidity(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }
        }
    }


}
