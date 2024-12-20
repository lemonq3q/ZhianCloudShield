package org.example.service.impl;


import com.alibaba.fastjson.JSON;
import org.example.annotation.Slave;
import org.example.annotation.SystemControllerLog;
import org.example.entity.*;
import org.example.mapper.HumiditysMapper;
import org.example.mapper.MeterMapper;
import org.example.mapper.ProductMapper;
import org.example.mapper.TemperaturesMapper;
import org.example.resp.ResultData;
import org.example.resp.ReturnCodeEnum;
import org.example.service.DataService;
import org.example.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DataImpl implements DataService {

    /**private NowTemperatureMapper nowTemperatureMapper;**/

    @Autowired
    private TemperaturesMapper temperaturesMapper;

    /**private NowHumidityMapper nowHumidityMapper;**/

    @Autowired
    private HumiditysMapper humiditysMapper;

    /**private NowPositionMapper nowPositionMapper;**/

    /**private RiskMapper riskMapper;**/

    /**private Workshop_deviceIdMapper workshopDeviceIdMapper;**/

    /**private WorkerNumMapper workerNumMapper;**/

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private MeterMapper meterMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    //根据厂房号获取当前温度
    public ResultData getNowTemperature(String workshop) {
        String nowTemperature = (String) redisTemplate.opsForValue().get(workshop+":now_temperature");
        if(nowTemperature!=null){
            Temperature result = JSON.parseObject(nowTemperature,Temperature.class);
            return ResultData.success(result);
        }
        return ResultData.fail(ReturnCodeEnum.RC999.getCode(),"no temperature data of "+workshop);
    }



    @Override
    //获取当前湿度
    public ResultData getNowHumidity(String workshop) {
        String nowHumidity = (String) redisTemplate.opsForValue().get(workshop+":now_humidity");
        if(nowHumidity!=null){
            Humidity result = JSON.parseObject(nowHumidity,Humidity.class);
            return ResultData.success(result);
        }
        return ResultData.fail(ReturnCodeEnum.RC999.getCode(),"no humidity data of "+workshop);
    }


    @Override
    @Slave
    @SystemControllerLog()
    //分页查询，分页获取当前温度数据
    public ResultData getTemperatureByPage(String workshop, int startNumber, int endNumber) {
        Set<String> idSet = redisTemplate.opsForZSet().reverseRange(workshop+":temperature_id",startNumber,endNumber);
        //不存在该页数据
        if(idSet==null || idSet.size()<1){
            return ResultData.fail(ReturnCodeEnum.RC999.getCode(),"no temperature data of "+workshop);
        }
        List<String> resultList = redisTemplate.opsForValue().multiGet(idSet);
        //存储存在的结果
        List<Temperature> existResult = new ArrayList<>();
        //存储不存在的id
        List<Integer> missingIds = new ArrayList<>();
        Iterator<String> idSetIterator = idSet.iterator();
        for(String result : resultList){
            String id = idSetIterator.next();
            if(result == null){
                missingIds.add(Integer.parseInt(id.split(":")[1]));
            }
            else {
                existResult.add(JSON.parseObject(result,Temperature.class));
            }
        }
        //声明返回结果
        List<Temperature> temperaturesList = new ArrayList<>();
        List<Temperature> missingResult = new ArrayList<>();
        if(missingIds.size()>0){
            //获取不存在的id对应的数据
            System.out.println("查询mysql");
            missingResult = temperaturesMapper.getTemperatureByIdList(missingIds);
            //将数据批量存入redis
            Map<String,String> redisData = new HashMap<>();
            for(Temperature result : missingResult){
                redisData.put("temperature:"+result.getId(), JSON.toJSONString(result));
            }
            RedisUtil.multiSetValue(redisTemplate,redisData,2000);
        }
        //合并该页结果
        Iterator<Temperature> iterator = missingResult.iterator();
        Temperature missingTmp = null;
        if(iterator.hasNext()){
            missingTmp = iterator.next();
        }
        for (int i = 0; i < existResult.size();) {
            if(missingTmp!=null && missingTmp.getId()>=existResult.get(i).getId()){
                temperaturesList.add(missingTmp);
                if(iterator.hasNext()){
                    missingTmp = iterator.next();
                }
                else{
                    missingTmp = null;
                }
            }
            else{
                temperaturesList.add(existResult.get(i));
                i++;
            }
        }
        while(iterator.hasNext()){
            temperaturesList.add(iterator.next());
        }
        return ResultData.success(temperaturesList);
    }

    @Override
    @Slave
    //分页查询，分页获取当前湿度数据
    public ResultData getHumidityByPage(String workshop, int startNumber, int endNumber) {
        Set<String> idSet = redisTemplate.opsForZSet().reverseRange(workshop+":humidity_id",startNumber,endNumber);
        //不存在该页数据
        if(idSet==null || idSet.size()<1){
            return ResultData.fail(ReturnCodeEnum.RC999.getCode(),"no humidity data of "+workshop);
        }
        List<String> resultList = redisTemplate.opsForValue().multiGet(idSet);
        //存储存在的结果
        List<Humidity> existResult = new ArrayList<>();
        //存储不存在的id
        List<Integer> missingIds = new ArrayList<>();
        Iterator<String> idSetIterator = idSet.iterator();
        for(String result : resultList){
            String id = idSetIterator.next();
            if(result == null){
                missingIds.add(Integer.parseInt(id.split(":")[1]));
            }
            else {
                existResult.add(JSON.parseObject(result,Humidity.class));
            }
        }
        //声明返回结果
        List<Humidity> humiditiesList = new ArrayList<>();
        List<Humidity> missingResult = new ArrayList<>();
        if(missingIds.size()>0){
            //获取不存在的id对应的数据
            System.out.println("查询mysql");
            missingResult = humiditysMapper.getHumidityByIdList(missingIds);
            //将数据批量存入redis
            Map<String,String> redisData = new HashMap<>();
            for(Humidity result : missingResult){
                redisData.put("humidity:"+result.getId(), JSON.toJSONString(result));
            }
            RedisUtil.multiSetValue(redisTemplate,redisData,2000);
        }
        //合并该页结果
        Iterator<Humidity> iterator = missingResult.iterator();
        Humidity missingTmp = null;
        if(iterator.hasNext()){
            missingTmp = iterator.next();
        }
        for (int i = 0; i < existResult.size();) {
            if(missingTmp!=null && missingTmp.getId()>=existResult.get(i).getId()){
                humiditiesList.add(missingTmp);
                if(iterator.hasNext()){
                    missingTmp = iterator.next();
                }
                else{
                    missingTmp = null;
                }
            }
            else{
                humiditiesList.add(existResult.get(i));
                i++;
            }
        }
        while(iterator.hasNext()){
            humiditiesList.add(iterator.next());
        }
        return ResultData.success(humiditiesList);
    }

    @Override
    public List<Meter> getNewDataByWorkshop() {
        List<Meter> dataList = new ArrayList<>();
        Meter meter;
        meter = meterMapper.getNewDataByWorkshop("1");
        meter.setValue(Fixed3(meter.getValue()));
        dataList.add(meter);
        meter = meterMapper.getNewDataByWorkshop("2");
        meter.setValue(Fixed3(meter.getValue()));
        dataList.add(meter);
        meter = meterMapper.getNewDataByWorkshop("3");
        meter.setValue(Fixed3(meter.getValue()));
        dataList.add(meter);
        meter = meterMapper.getNewDataByWorkshop("4");
        meter.setValue(Fixed3(meter.getValue()));
        dataList.add(meter);
        return dataList;
    }

    @Override
    public float Fixed3(float num) {
        int x = (int)num;
        num = num - x;
        float result = 0;
        while(result + 0.125 < num){
            result += 0.125;
        }
        return result+x;
    }


}
