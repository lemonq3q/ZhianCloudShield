package org.example.controller;


import org.example.entity.*;
import org.example.resp.ResultData;
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
    public ResultData getNowTemperature(String workshop){

        return dataService.getNowTemperature(workshop);
    }


    /**
     * 获取当前湿度
     * @param workshop
     * @return
     */
    @GetMapping("/getData/getNowHumidity")
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
    @GetMapping("/getData/getTemperatureByPage")
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
    @GetMapping("/getData/getHumidityByPage")
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

    public float Fixed3(float num){
        return dataService.Fixed3(num);
    }

}
