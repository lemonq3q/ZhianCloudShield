package org.example.controller;

import org.example.entity.OnenetData;
import org.example.service.OneNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OneNetController {

    @Autowired
    private OneNetService oneNetService;

    /**
     * 考勤信息处理
     * @param data
     * @return
     */
    @PostMapping("/attendanceRecord")
    public String getAttendanceRecord(@RequestBody OnenetData data) {
       return oneNetService.getAttendanceRecord(data);
    }

    /**
     * 温度接口验证
     * @param msg
     * @param nonce
     * @param signature
     * @return
     */
    @GetMapping ("/uploadTemp")
    public String getTemperatureValidate(String msg,String nonce,String signature) {
        return oneNetService.getTemperatureValidate(msg,nonce,signature);
    }

    /**
     * 湿度接口验证
     * @param msg
     * @param nonce
     * @param signature
     * @return
     */
    @GetMapping ("/uploadHum")
    public String getHumidityValidate(String msg,String nonce,String signature) {
        return oneNetService.getHumidityValidate(msg,nonce,signature);
    }

    /**
     * 考勤接口验证
     * @param msg
     * @param nonce
     * @param signature
     * @return
     */
    @GetMapping ("/attendanceRecord")
    public String getAttendanceRecordValidate(String msg,String nonce,String signature) {
        return oneNetService.getAttendanceRecordValidate(msg,nonce,signature);
    }

}
