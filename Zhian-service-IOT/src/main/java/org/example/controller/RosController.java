package org.example.controller;

import org.example.entity.InspectPoint;
import org.example.entity.Region;

import org.example.resp.ResponseMessage;
import org.example.service.RosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RosController {

    @Autowired
    private RosService rosService;

    /**
     * 更新区域信息
     * @param region
     * @return
     */
    @PostMapping("/ros/updateRegion")
    public ResponseMessage updateRegion(@RequestBody Region region){
        return rosService.updateRegion(region);
    }

    /**
     * 通过区域获取地图元信息
     * @param workshop
     * @return
     */
    @GetMapping("/ros/getRegion")
    public ResponseMessage getRegion(String workshop){
       return rosService.getRegion(workshop);
    }

    /**
     * 通过区域获取巡检点
     * @param workshop
     * @return
     */
    @GetMapping("/ros/getInspectPoints")
    public ResponseMessage getInspectPoints(String workshop){
        return rosService.getInspectPoints(workshop);
    }

    /**
     * 更新巡检点
     * @param inspectPoints
     * @return
     */
    @PostMapping("/ros/updatePoints")
    public ResponseMessage updateInspectPoints(@RequestBody InspectPoint[] inspectPoints){
        return rosService.updateInspectPoints(inspectPoints);
    }
}
