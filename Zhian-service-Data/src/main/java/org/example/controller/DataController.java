package org.example.controller;


import org.example.entity.*;
import org.example.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Position;
import java.util.List;
import java.util.Map;

@RestController
public class DataController {

    @Autowired
    private DataService dataService;


    /**
     * 获取即时温度
     * @param workshop
     * @return
     */
    @GetMapping("/getData/getNowTemperature")
    public Temperature getNowTemperature(String workshop){

        /**return dataService.getNowTemperature(workshop);**/
        return null;
    }

    /**
     * 获取某时间段内温度
     * @param workshop
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/getData/getTemperatureByTime")
    public List<Temperature> getTemperatureByTime(String workshop,long startTime,long endTime){
        return dataService.getTemperatureByTime(workshop,startTime,endTime);
    }

    /**
     * 获取当前湿度
     * @param workshop
     * @return
     */
    @GetMapping("/getData/getNowHumidity")
    public Humidity getNowHumidity(String workshop){
        return dataService.getNowHumidity(workshop);
    }

    /**
     * 获取某时间段内湿度
     * @param workshop
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/getData/getHumidityByTime")
    public List<Humidity> getHumidityByTime(String workshop,long startTime,long endTime){
        return dataService.getHumidityByTime(workshop,startTime,endTime);
    }

    /**
     * 分页获取最近的温度
     * @param workshop
     * @param startNumber
     * @param endNumber
     * @return
     */
    @GetMapping("/getData/getTemperatureByPage")
    public List<Temperature> getTemperatureByPage(String workshop,int startNumber,int endNumber){
        return dataService.getTemperatureByPage(workshop,startNumber,endNumber);
    }

    /**
     * 分页获取最近湿度
     * @param workshop
     * @param startNumber
     * @param endNumber
     * @return
     */
    @GetMapping("/getData/getHumidityByPage")
    public List<Humidity> getHumidityByPage(String workshop,int startNumber,int endNumber){
        return dataService.getHumidityByPage(workshop,startNumber,endNumber);
    }

    /**
     * 获取所有的人员的当前位置
     * @return
     */
    @GetMapping("/getData/getWorkersIdAndLocation")
    public List<NowPosition> getWorkersIdAndLocation(){
        return dataService.getWorkersIdAndLocation();
    }

    /**
     * 获取某工厂内打卡员工
     * @param workshop
     * @return
     */
    @GetMapping("/getData/getWorkersByWorkshop")
    public List<Employee> getWorkersByWorkshop(String workshop){
        return dataService.getWorkersByWorkshop(workshop);
    }

    /**
     * 获取某工厂内的动态考勤数量
     * @return
     */
    @GetMapping("/getData/getWorkerNumByWorkshop")
    public int getWorkerNumByWorkshop(String workshop){
        return dataService.getWorkerNumByWorkshop(workshop);
    }

    /**
     * 获取某工厂内的动态考勤数量
     * @return
     */
    @GetMapping("/getData/getWorkerAllNum")
    public List<WorkerNum> getAllWorkerNumB(){
        return dataService.getAllWorkerNumB();
    }



    /**
     * 获取全部的工厂信息
     * @return
     */
    @GetMapping("/getData/getWorkshop")
    public List<Workshop_deviceId> getWorkshop(){
        return dataService.getWorkshop();
    }



    /**
     * 获取所有产品信息
     * @return
     */
    @GetMapping("/getData/getAllProduct")
    public List<Product> getAllProduct() {
        return dataService.getAllProduct();
    }

    /**
     * 插入仪表读数信息
     * @param meter
     * @return
     */
    @PostMapping("/meter/addMeterData")
    public String addMeterData(@RequestBody Meter meter){
        return dataService.addMeterData(meter);
    }

    /**
     * 获取所有区域当前仪表盘数据
     * @return
     */
    @GetMapping("/meter/getNewDataByWorkshop")
    public List<Meter> getNewDataByWorkshop(){
        return dataService.getNewDataByWorkshop();
    }

    public float Fixed3(float num){
        return dataService.Fixed3(num);
    }

}
