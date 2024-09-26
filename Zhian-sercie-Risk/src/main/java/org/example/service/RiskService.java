package org.example.service;

import org.example.entity.Risk;
import org.example.resp.ResultData;

import java.util.List;
import java.util.Map;

public interface RiskService {
    //分页获取风险记录
    public List<Risk> getRiskRecord(int startNumber, int endNumber);

    //根据厂房号获取风险记录
    public List<Risk> getRiskRecordByWorkshop(String workshop,int startNumber,int endNumber);

    //根据风险id查询风险信息
    public Risk getRiskById(int id);

    //根据车间号和风险等级获取风险数据
    public List<Risk> getRiskByWorkshopAndLevel(String workshop,int level);

    //修改风险描述
    public String addRiskDescription(int id,String description);

    //删除风险记录
    public String deleteRisk(int id);

    //时间和等级获取风险数据
    public int getRiskNumByLevel(long startTime,long endTime,int level);

    //返回整个风险记录数量-以等级划分
    public Map<String,Integer> getRiskNum();

    //时间和风险名称获取风险数据
    public int getRiskNumByRiskName(long startTime,long endTime,String riskName);

    public ResultData addRisk(Risk risk);
}
