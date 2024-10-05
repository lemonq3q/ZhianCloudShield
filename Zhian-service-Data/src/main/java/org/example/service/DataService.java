package org.example.service;



import org.apache.logging.log4j.message.ReusableMessage;
import org.example.entity.*;
import org.example.resp.ResultData;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;


public interface DataService {

    //获取当前温度
    public ResultData getNowTemperature(String workshop);

    //当前湿度
    public ResultData getNowHumidity(String workshop);

    //分页获取温度数据 limit..
    public ResultData getTemperatureByPage(String workshop, int startNumber, int endNumber);

    //分页获取湿度数据
    public ResultData getHumidityByPage(String workshop, int startNumber, int endNumber);

    public List<Meter> getNewDataByWorkshop();

    public float Fixed3(float num);
}
