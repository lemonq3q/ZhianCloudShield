package org.example.service.impl;

import org.example.entity.OldOnenetMsg;
import org.example.entity.OnenetData;
import org.example.service.OneNetService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
public class OneNetImpl implements OneNetService {

    private static final String ID = "id";
    private static final String FINGER = "fingerid";
    private static final String TEMPERATURE = "Temp";
    private static final String HUMIDITY = "Humi";

    @Override
    public String getAttendanceRecord(OnenetData data) {

        OldOnenetMsg oldOnenetMsg = data.getMsg();
        if(oldOnenetMsg.getType()==1){
            if(oldOnenetMsg.getDs_id().equals(ID) || oldOnenetMsg.getDs_id().equals(FINGER)){
                AttendanceHandler(oldOnenetMsg);
            } else if(oldOnenetMsg.getDs_id().equals(TEMPERATURE)){
                TemperatureHandler(oldOnenetMsg);
            } else if(oldOnenetMsg.getDs_id().equals(HUMIDITY)){
                HumidityHandler(oldOnenetMsg);
            }
        }
        else if(oldOnenetMsg.getType()==2){
            DeviceHandler(oldOnenetMsg);
        }
        return "over";
    }

    @Override
    public String getTemperatureValidate(String msg, String nonce, String signature) {
        return msg;
    }

    @Override
    public String getHumidityValidate(String msg, String nonce, String signature) {
        return msg;
    }

    @Override
    public String getAttendanceRecordValidate(String msg, String nonce, String signature) {
        return msg;
    }



    private void TemperatureHandler(OldOnenetMsg oldOnenetMsg){
//        //更新温度
//        String workshop = deviceMapper.getWorkshopByDevice_id(Integer.toString(oldOnenetMsg.getDev_id()));
//        if(nowTemperatureMapper.getTemperatureNum(workshop)<1){
//            nowTemperatureMapper.insertTemperature(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }else {
//            nowTemperatureMapper.updateTemperature(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }
//        if(oldOnenetMsg.getAt()-temperaturesMapper.getTemperatureLastTime(workshop)>2*1000){
//            temperaturesMapper.insertTemperature(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }

    }

    private void HumidityHandler(OldOnenetMsg oldOnenetMsg){
//        //更新湿度
//        String workshop = deviceMapper.getWorkshopByDevice_id(Integer.toString(oldOnenetMsg.getDev_id()));
//        if(nowHumidityMapper.getHumidityNum(workshop)<1){
//            nowHumidityMapper.insertHumidity(workshop,oldOnenetMsg.getAt(),Float.parseFloat(oldOnenetMsg.getValue()));
//        }else {
//            nowHumidityMapper.updateHumidity(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }
//        if(oldOnenetMsg.getAt()-humiditysMapper.getHumiLastTime(workshop)>2*1000){
//            humiditysMapper.insertHumidity(workshop,Float.parseFloat(oldOnenetMsg.getValue()),oldOnenetMsg.getAt());
//        }
    }

    private void AttendanceHandler(OldOnenetMsg oldOnenetMsg){
//        if(oldOnenetMsg.getDs_id().equals(FINGER)){
//            String cardId = employeeMapper.getCardIdById(Integer.parseInt(oldOnenetMsg.getValue()));
//            oldOnenetMsg.setValue(cardId);
//        }
//        AttendanceUpdate(oldOnenetMsg);
    }

    private void DeviceHandler(OldOnenetMsg oldOnenetMsg){
    }
}

