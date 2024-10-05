package org.example.service.impl;


import com.alibaba.fastjson.JSON;
import org.example.entity.*;
import org.example.mapper.HumiditysMapper;
import org.example.mapper.MeterMapper;
import org.example.mapper.ProductMapper;
import org.example.mapper.TemperaturesMapper;
import org.example.resp.ResultData;
import org.example.resp.ReturnCodeEnum;
import org.example.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

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
        for(String result : resultList){
            String id = idSet.iterator().next();
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
            missingResult = temperaturesMapper.getTemperatureByIdList(missingIds);
            //将数据批量存入redis
            Map<String,String> redisData = new HashMap<>();
            for(Temperature result : missingResult){
                redisData.put("temperature:"+result.getId(), JSON.toJSONString(result));
            }
            redisTemplate.opsForValue().multiSet(redisData);
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
        for(String result : resultList){
            String id = idSet.iterator().next();
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
            missingResult = humiditysMapper.getHumidityByIdList(missingIds);
            //将数据批量存入redis
            Map<String,String> redisData = new HashMap<>();
            for(Humidity result : missingResult){
                redisData.put("humidity:"+result.getId(), JSON.toJSONString(result));
            }
            redisTemplate.opsForValue().multiSet(redisData);
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
    //获取所有人员的当前位置
    public List<NowPosition> getWorkersIdAndLocation() {
        List<NowPosition> workerList = nowPositionMapper.getNowPosition();
        return workerList;
    }

    @Override
    //获取工厂内打卡的员工
    public List<Employee> getWorkersByWorkshop(String workshop) {
        if(workshop=="")return null;
        List<Employee> workerList = nowPositionMapper.getWorkersByWorkshop(workshop);
        return workerList;
    }

    @Override
    //获取工厂内的动态考勤数量
    public int getWorkerNumByWorkshop(String workshop) {
        if(workshop=="")return 0;
        return workerNumMapper.getWorkerNumByWorkshop(workshop);
    }

    @Override
    //获取某工厂内的动态考勤数量
    public List<WorkerNum> getAllWorkerNumB() {
        List<WorkerNum> numList = new ArrayList<>();
        WorkerNum workerNum1 = new WorkerNum("1",workerNumMapper.getWorkerNumByWorkshop("1"));
        numList.add(workerNum1);
        WorkerNum workerNum2 = new WorkerNum("2",workerNumMapper.getWorkerNumByWorkshop("2"));
        numList.add(workerNum2);
        WorkerNum workerNum3 = new WorkerNum("3",workerNumMapper.getWorkerNumByWorkshop("3"));
        numList.add(workerNum3);
        WorkerNum workerNum4 = new WorkerNum("4",workerNumMapper.getWorkerNumByWorkshop("4"));
        numList.add(workerNum4);
        return numList;
    }



    @Override
    //获取全部的工厂信息
    public List<Workshop_deviceId> getWorkshop() {
        List<Workshop_deviceId> workshopList = workshopDeviceIdMapper.getWorkshop();
        return workshopList;
    }


    @Override
    //获取所有产品信息
    public List<Product> getAllProduct() {
        return productMapper.getAllProduct();
    }

    @Override
    public String addMeterData(Meter meter) {
        try{
            long time = System.currentTimeMillis();
            String workshop = deviceMapper.getWorkshopByDevice_id(meter.getWorkshop());
            meterMapper.insertMeterData(workshop,meter.getType(),time,meter.getUnit(),meter.getValue());
            return "succeed";
        }catch (Exception e){
            return "failed";
        }
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
