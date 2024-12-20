package org.example.api;

import org.example.entity.Model;
import org.example.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("Zhian-service-Model")
public interface ModelApi {
    @GetMapping("/model/updateDeviceModel")
    public String updateDeviceModel(@RequestParam(value = "oldDevice_id") String oldDevice_id,@RequestParam(value = "newDevice_id") String newDevice_id);

    @DeleteMapping("/model/deleteDeivceBind")
    public String deleteDeviceBind(String device_id);

    @GetMapping("/model/getModelByDevice")
    public ResultData getModelByDevice(String device_id);
}
