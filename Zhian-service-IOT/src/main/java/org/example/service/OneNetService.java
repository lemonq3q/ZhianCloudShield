package org.example.service;

import org.example.entity.Meter;
import org.example.entity.OldOnenetMsg;
import org.example.entity.OnenetData;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface OneNetService {

    public String getAttendanceRecord(@RequestBody OnenetData data);

    public String getTemperatureValidate(String msg,String nonce,String signature);

    public String getHumidityValidate(String msg,String nonce,String signature);

    public String getAttendanceRecordValidate(String msg,String nonce,String signature);

}

