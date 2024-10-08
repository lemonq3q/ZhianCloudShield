package org.example.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.example.api.DeviceApi;
import org.example.config.RabbitmqNameConfig;
import org.example.entity.Humidity;
import org.example.entity.Temperature;
import org.example.mapper.HumiditysMapper;
import org.example.mapper.TemperaturesMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class OneNetListener extends RabbitmqNameConfig {

    @Autowired
    DeviceApi deviceApi;

    @Autowired
    TemperaturesMapper temperaturesMapper;

    @Autowired
    HumiditysMapper humiditysMapper;


    @Autowired
    RedisTemplate redisTemplate;

    @RabbitListener(queues = {TEMP_QUEUE})
    public void TemperatureProcess(Message message, Channel channel) {
        String jsonString = new String(message.getBody());
        Map<String, String> dataMap = JSON.parseObject(jsonString, Map.class);
        String workshop = deviceApi.getWorkshop(dataMap.get("dev_id")).getData();
        long at = Integer.parseInt(dataMap.get("at"));

        if (at - temperaturesMapper.getTemperatureLastTime(workshop) > 2 * 1000) {
            Temperature temperature = new Temperature(0, workshop, at, Float.parseFloat(dataMap.get("value")));
            temperaturesMapper.insertTemperature(temperature);
            //更新redis
            redisTemplate.opsForValue().set(workshop+":now_temperature", JSON.toJSONString(temperature));
            redisTemplate.opsForZSet().add(workshop+":temperature_id","temperature:"+temperature.getId(),temperature.getId());
            redisTemplate.opsForValue().set("temperature:"+temperature.getId(), JSON.toJSONString(temperature),2000, TimeUnit.SECONDS);
        }

    }

    @RabbitListener(queues = {HUM_QUEUE})
    public void HumidityProcess(Message message, Channel channel) {
        String jsonString = new String(message.getBody());
        Map<String, String> dataMap = JSON.parseObject(jsonString, Map.class);
        String workshop = deviceApi.getWorkshop(dataMap.get("dev_id")).getData();
        long at = Integer.parseInt(dataMap.get("at"));

        if(at-humiditysMapper.getHumiLastTime(workshop)>2*1000) {
            Humidity humidity = new Humidity(0, workshop, at, Float.parseFloat(dataMap.get("value")));
            humiditysMapper.insertHumidity(workshop, Float.parseFloat(dataMap.get("value")), at);
            //更新redis
            redisTemplate.opsForValue().set(workshop+":now_humidity", JSON.toJSONString(humidity));
            redisTemplate.opsForZSet().add(workshop+":humidity_id","humidity:"+humidity.getId(),humidity.getId());
            redisTemplate.opsForValue().set("humidity:"+humidity.getId(), JSON.toJSONString(humidity),2000, TimeUnit.SECONDS);
        }
    }


}
