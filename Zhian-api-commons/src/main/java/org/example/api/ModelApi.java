package org.example.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("Zhian-service-Model")
public interface ModelApi {
    @GetMapping("/model/updateDeviceModel")
    public String updateDeviceModel(String oldDevice_id,String newDevice_id);

    @DeleteMapping("/model/deleteDeivceBind")
    public String deleteDeviceBind(String device_id);
}
