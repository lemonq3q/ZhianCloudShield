package org.example.service;

import org.example.entity.NowPosition;
import org.example.resp.ResultData;

public interface PushService {
    public ResultData<String> AttendancePush(NowPosition sendMessage);
}
