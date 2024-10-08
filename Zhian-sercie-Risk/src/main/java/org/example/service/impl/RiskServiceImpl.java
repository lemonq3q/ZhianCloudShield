package org.example.service.impl;

import org.example.annotation.Master;
import org.example.annotation.Slave;
import org.example.entity.Risk;
import org.example.resp.ResultData;
import org.example.resp.ReturnCodeEnum;
import org.example.service.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.example.dao.RiskMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RiskServiceImpl implements RiskService {

    @Autowired
    private RiskMapper riskMapper;

    @Override
    //分页获取风险记录
    public List<Risk> getRiskRecord(int startNumber, int endNumber) {
        if(endNumber==0) return null;
        List<Risk> riskList = riskMapper.getRiskByPage(startNumber,endNumber);
        return riskList;
    }

    @Override
    //根据车间号查询风险记录
    public List<Risk> getRiskRecordByWorkshop(String workshop, int startNumber, int endNumber) {
        if(workshop==""||endNumber==0)return null;
        List<Risk> riskList = riskMapper.getRiskByWorkshop(workshop,startNumber,endNumber);
        return riskList;
    }

    @Override
    //根据风险编号查询
    public Risk getRiskById(int id) {
        if(id==0) return null;
        Risk risk = riskMapper.getRiskById(id);
        return risk;
    }

    @Override
    //根据车间号和风险等级获取风险记录
    public List<Risk> getRiskByWorkshopAndLevel(String workshop, int level) {
        if(workshop==""||level==0)return null;
        List<Risk> riskList = riskMapper.getRiskByWorkshopAndLevel(workshop,level);
        return riskList;
    }

    @Override
    //添加风险描述
    public String addRiskDescription(int id, String description) {
        int num = riskMapper.addRiskDescription(id,description);
        if(num>0){
            return "succeed";
        }
        else{
            return "failed";
        }
    }

    @Override
    //删除风险
    public String deleteRisk(int id) {
        int num = riskMapper.deleteRisk(id);
        if(num>0){
            return "succeed";
        }
        else{
            return "failed";
        }
    }

    @Override
    //获取某段时间内发生的各个等级的风险数量
    public int getRiskNumByLevel(long startTime, long endTime, int level) {
        if(level==0)return 0;
        int num;
        if(level==0){
            num = riskMapper.getRiskNumByLevel(startTime,endTime,1)
                    +riskMapper.getRiskNumByLevel(startTime,endTime,2)
                    +riskMapper.getRiskNumByLevel(startTime,endTime,3)
                    +riskMapper.getRiskNumByLevel(startTime,endTime,4);
        }
        else{
            num = riskMapper.getRiskNumByLevel(startTime,endTime,level);
        }
        return num;
    }

    @Override
    //获取历史风险数量记录
    //即从开始到现在，数据库中的所有风险记录数量
    public Map<String, Integer> getRiskNum() {
        //映射容器
        Map<String,Integer> riskNumMap = new HashMap<>();
        long nowTime = System.currentTimeMillis();
        int sum = riskMapper.getRiskNumByLevel(0,nowTime,1)
                +riskMapper.getRiskNumByLevel(0,nowTime,2)
                +riskMapper.getRiskNumByLevel(0,nowTime,3)
                +riskMapper.getRiskNumByLevel(0,nowTime,4);
        //存入值
        riskNumMap.put("0",sum);
        riskNumMap.put("1",riskMapper.getRiskNumByLevel(0,nowTime,1));
        riskNumMap.put("2",riskMapper.getRiskNumByLevel(0,nowTime,2));
        riskNumMap.put("3",riskMapper.getRiskNumByLevel(0,nowTime,3));
        riskNumMap.put("4",riskMapper.getRiskNumByLevel(0,nowTime,4));
        return riskNumMap;
    }

    @Override
    //获取某段时间内风险发生的次数
    public int getRiskNumByRiskName(long startTime, long endTime, String riskName) {
        if(startTime==endTime||endTime>startTime)return 0;
        int num = riskMapper.getRiskNumByRiskName(startTime,endTime,riskName);
        return num;
    }

    @Override
    public ResultData addRisk(Risk risk){
        int num = riskMapper.insertRisk(risk);
        if(num==0){
            return ResultData.fail(ReturnCodeEnum.RC999.getCode(), "添加数据失败");
        }
        return ResultData.success("添加数据成功");
    }
}
