package org.example.api;

import org.example.entity.NowPosition;
import org.example.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("Zhian-service-push")
public interface PushApi {
    @PostMapping("/push/attendance")
    public ResultData<String> attendancePush(NowPosition sendMessage);
}
