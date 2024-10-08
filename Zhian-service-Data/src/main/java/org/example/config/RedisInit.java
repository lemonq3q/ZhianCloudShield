package org.example.config;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.PostConstruct;
import org.example.annotation.Slave;
import org.example.entity.Humidity;
import org.example.entity.Temperature;
import org.example.mapper.HumiditysMapper;
import org.example.mapper.TemperaturesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RedisInit {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    TemperaturesMapper temperaturesMapper;

    @Autowired
    HumiditysMapper humiditysMapper;

    @PostConstruct
    public void RedisInit() {
        TempRedisInit();
        HumiRedisInit();
    }

    @Slave
    public void TempRedisInit(){
        List<String> workshopList = temperaturesMapper.getWorkshopList();
        for(String workshop : workshopList){
            List<Integer> idList = temperaturesMapper.getIdList(workshop);
            Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
            for(int id : idList){
                ZSetOperations.TypedTuple<String> typedTuple = new DefaultTypedTuple<>("temperature:"+id,(double)id);
                typedTupleSet.add(typedTuple);
            }
            //更新温度id缓存列表
            redisTemplate.opsForZSet().add(workshop+":temperature_id",typedTupleSet);

            //更新当前温度
            Temperature temperature = temperaturesMapper.getLastTemperatureByWorkshop(workshop);
            redisTemplate.opsForValue().set(workshop+":now_temperature", JSON.toJSONString(temperature));
        }
    }

    @Slave
    public void HumiRedisInit(){
        List<String> workshopList = humiditysMapper.getWorkshopList();
        for(String workshop : workshopList){
            List<Integer> idList = humiditysMapper.getIdList(workshop);
            Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
            for(int id : idList){
                ZSetOperations.TypedTuple<String> typedTuple = new DefaultTypedTuple<>("humidity:"+id,(double)id);
                typedTupleSet.add(typedTuple);
            }
            //更新湿度id缓存列表
            redisTemplate.opsForZSet().add(workshop+":humidity_id",typedTupleSet);

            //更新当前湿度
            Humidity humidity = humiditysMapper.getLastHumidityByWorkshop(workshop);
            redisTemplate.opsForValue().set(workshop+":now_humidity", JSON.toJSONString(humidity));
        }
    }
}
