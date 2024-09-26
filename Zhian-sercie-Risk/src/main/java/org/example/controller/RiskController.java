package org.example.controller;

import org.example.entity.Risk;
import org.example.resp.ResultData;
import org.example.service.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RiskController {

    @Autowired
    private RiskService riskService;

    /**
     * 获取风险记录
     * @param startNumber
     * @param endNumber
     * @return
     */
    @GetMapping("/getData/getRiskRecord")
    public List<Risk> getRiskRecord(int startNumber, int endNumber){
        return riskService.getRiskRecord(startNumber,endNumber);
    }

    /**
     * 通过车间号获得风险记录
     * @param workshop
     * @param startNumber
     * @param endNumber
     * @return
     */
    @GetMapping("/getData/getRiskRecordByWorkshop")
    public List<Risk> getRiskRecordByWorkshop(String workshop,int startNumber,int endNumber){
        return riskService.getRiskRecordByWorkshop(workshop,startNumber,endNumber);
    }

    /**
     * 通过id获取风险记录
     * @param id
     * @return
     */
    @GetMapping("/getData/getRiskById")
    public Risk getRiskById(int id){
        return riskService.getRiskById(id);
    }

    /**
     * 通过车间号和风险等级获取风险记录
     * @param workshop
     * @param level
     * @return
     */
    @GetMapping("/getRiskByWorkshopAndLevel")
    public List<Risk> getRiskByWorkshopAndLevel(String workshop,int level){
        return riskService.getRiskByWorkshopAndLevel(workshop,level);
    }

    /**
     * 添加风险描述
     * @param id
     * @param description
     * @return
     */
    @PostMapping("/addData/addRiskDescription")
    public String addRiskDescription(int id,String description){
        return riskService.addRiskDescription(id,description);
    }

    /**
     * 删除风险
     * @param id
     * @return
     */
    @DeleteMapping("/addData/deleteRisk")
    public String deleteRisk(int id){
        return riskService.deleteRisk(id);
    }

    /**
     * 获取在某段时间内发生对应等级的风险数量
     * @param startTime
     * @param endTime
     * @param level
     * @return
     */
    @GetMapping("/getData/getRiskNumByLevel")
    public int getRiskNumByLevel(long startTime,long endTime,int level){
        return riskService.getRiskNumByLevel(startTime,endTime,level);
    }

    /**
     * 获取历史风险数量记录
     * @return
     */
    @GetMapping("/getData/getRiskNum")
    public Map<String,Integer> getRiskNum(){
        return riskService.getRiskNum();
    }

    /**
     * 获取某时间段内对应风险类型的发生次数
     * @param startTime
     * @param endTime
     * @param riskName
     * @return
     */
    @GetMapping("/getData/getRiskNumByRiskName")
    public int getRiskNumByRiskName(long startTime,long endTime,String riskName){
        return riskService.getRiskNumByRiskName(startTime,endTime,riskName);
    }

    /**
     * 添加风险
     * @param risk
     * @return
     */
    @PostMapping("/risk/addRisk")
    public ResultData addRisk(@RequestBody Risk risk){
        return riskService.addRisk(risk);
    }
}
