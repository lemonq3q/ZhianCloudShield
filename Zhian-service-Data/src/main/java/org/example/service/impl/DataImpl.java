package org.example.service.impl;


import org.example.entity.*;
import org.example.mapper.HumiditysMapper;
import org.example.mapper.MeterMapper;
import org.example.mapper.ProductMapper;
import org.example.mapper.TemperaturesMapper;
import org.example.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    //根据厂房号获取当前温度
    public Temperature getNowTemperature(String workshop) {
//        if(workshop=="") return null;
//        Temperature temperature = nowTemperatureMapper.getNowTemperature(workshop);
//        return temperature;
        return null;
    }

    @Override
    //获取一段时间内的温度
    public List<Temperature> getTemperatureByTime(String workshop, long startTime, long endTime) {
        if(workshop==""||startTime==0||endTime==0) return null;
        List<Temperature> tempList = temperaturesMapper.getTemperatureByTime(workshop,startTime,endTime);
        return tempList;
    }

    @Override
    //获取当前湿度
    public Humidity getNowHumidity(String workshop) {
//        if(workshop=="")return null;
//        Humidity humidity = nowHumidityMapper.getNowHumidity(workshop);
//        return humidity;
        return null;
    }

    @Override
    //获取一段时间内的湿度
    public List<Humidity> getHumidityByTime(String workshop, long startTime, long endTime) {
        if(workshop==""||startTime==0||endTime==0) return null;
        List<Humidity> humList = humiditysMapper.getTemperatureByTime(workshop,startTime,endTime);
        return humList;
    }

    @Override
    //分页查询，分页获取当前温度数据
    public List<Temperature> getTemperatureByPage(String workshop, int startNumber, int endNumber) {
        if(workshop==""||endNumber==0)return null;
        List<Temperature> tempList = temperaturesMapper.getTemperatureByPage(workshop,startNumber,endNumber);
        return tempList;
    }

    @Override
    //分页查询，分页获取当前湿度数据
    public List<Humidity> getHumidityByPage(String workshop, int startNumber, int endNumber) {
        if(workshop==""||endNumber==0)return null;
        List<Humidity> humList = humiditysMapper.getHumidityByPage(workshop,startNumber,endNumber);
        return humList;
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
