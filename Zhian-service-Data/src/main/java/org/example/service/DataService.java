package org.example.service;



import org.example.entity.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;


public interface DataService {

    //获取当前温度
    public Temperature getNowTemperature(String workshop);

    //获取一段时间内的温度 between...and...
    public List<Temperature> getTemperatureByTime(String workshop, long startTime, long endTime);

    //当前湿度
    public Humidity getNowHumidity(String workshop);

    //一段时间内的湿度
    public List<Humidity> getHumidityByTime(String workshop, long startTime, long endTime);

    //分页获取温度数据 limit..
    public List<Temperature> getTemperatureByPage(String workshop, int startNumber, int endNumber);

    //分页获取湿度数据
    public List<Humidity> getHumidityByPage(String workshop, int startNumber, int endNumber);

    //获取当前员工位置信息
    public List<NowPosition> getWorkersIdAndLocation();

    //根据厂房号查询员工数据
    public List<Employee> getWorkersByWorkshop(String workshop);

    //根据厂房号获取在线员工数量
    public int getWorkerNumByWorkshop(String workshop);

    //获取各个厂房员工数目的数组
    public List<WorkerNum> getAllWorkerNumB();

    public List<Workshop_deviceId> getWorkshop();

    //厂房的产品信息
    public List<Product> getAllProduct();

    public String addMeterData(@RequestBody Meter meter);

    public List<Meter> getNewDataByWorkshop();

    public float Fixed3(float num);
}
