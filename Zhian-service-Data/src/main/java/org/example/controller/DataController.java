package org.example.controller;


import org.example.annotation.Master;
import org.example.annotation.Slave;
import org.example.annotation.SystemControllerLog;
import org.example.entity.*;
import org.example.mapper.TemperaturesMapper;
import org.example.resp.ResultData;
import org.example.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Position;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
public class DataController {

    @Autowired
    private DataService dataService;
    @Autowired
    private TemperaturesMapper temperaturesMapper;


    @GetMapping("/data/delay")
    public ResultData delayTest() throws InterruptedException {
        Random random = new Random();
        int delayTime = (int)(2 + 4 * random.nextDouble())*1000;
        Thread.sleep(delayTime);
        return ResultData.success("success");
    }

    /**
     * 获取即时温度
     * @param workshop
     * @return
     */
    @SystemControllerLog(operation = "test" , type = "info")
    @GetMapping("/data/getNowTemperature")
    public ResultData getNowTemperature(String workshop){

        return dataService.getNowTemperature(workshop);
    }


    /**
     * 获取当前湿度
     * @param workshop
     * @return
     */
    @SystemControllerLog(operation = "test" , type = "info")
    @GetMapping("/data/getNowHumidity")
    public ResultData getNowHumidity(String workshop){
        return dataService.getNowHumidity(workshop);
    }



    /**
     * 分页获取最近的温度
     * @param workshop
     * @param startNumber
     * @param endNumber
     * @return
     */
    //@SystemControllerLog(operation = "test" , type = "info")
    @GetMapping("/data/getTemperatureByPage")
    public ResultData getTemperatureByPage(String workshop,int startNumber,int endNumber){
        return dataService.getTemperatureByPage(workshop,startNumber,endNumber);
    }

    /**
     * 分页获取最近湿度
     * @param workshop
     * @param startNumber
     * @param endNumber
     * @return
     */
    @SystemControllerLog(operation = "test" , type = "info")
    @GetMapping("/data/getHumidityByPage")
    public ResultData getHumidityByPage(String workshop,int startNumber,int endNumber){
        return dataService.getHumidityByPage(workshop,startNumber,endNumber);
    }

    /**
     * 获取所有区域当前仪表盘数据
     * @return
     */
    @GetMapping("/meter/getNewDataByWorkshop")
    public List<Meter> getNewDataByWorkshop(){
        return dataService.getNewDataByWorkshop();
    }

    @GetMapping("/data/getTest")
    @Slave
    public ResultData getTest(){
        return ResultData.success(temperaturesMapper.getTest());
    }

    @Master
    @PostMapping("/data/postTest")
    public ResultData postTest(float temperature){
        return ResultData.success(temperaturesMapper.updateTest(temperature));
    }


    public float Fixed3(float num){
        return dataService.Fixed3(num);
    }

}
