package org.example.controller;

import org.example.entity.NowPosition;
import org.example.resp.ResultData;
import org.example.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PushController {

    @Autowired
    PushService pushService;

    @PostMapping("/push/attendance")
    public ResultData<String> attendancePush(NowPosition sendMessage){
        return pushService.AttendancePush(sendMessage);
    }
}
