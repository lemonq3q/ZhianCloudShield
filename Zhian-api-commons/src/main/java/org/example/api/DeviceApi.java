package org.example.api;

import org.example.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("Zhian-service-device")
public interface DeviceApi {
    @GetMapping("/device/workshop")
    public ResultData<String> getWorkshop(String deviceId);
}
