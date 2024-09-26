package org.example.api;

import org.example.entity.Risk;
import org.example.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("Zhian-service-Risk")
public interface RiskAPi {
    @PostMapping("/risk/addRisk")
    public ResultData addRisk(@RequestBody Risk risk);
}
