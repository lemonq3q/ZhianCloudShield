package org.example.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("Zhian-service-device")
public interface DeviceApi {
    
}
