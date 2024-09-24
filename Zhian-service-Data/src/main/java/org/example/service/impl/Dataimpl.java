package org.example.service.impl;

import org.example.entity.*;
import org.example.mapper.*;
import org.example.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Dataimpl implements DataService {
    @Autowired
    private NowTemperatureMapper nowTemperatureMapper;

    @Autowired
    private TemperaturesMapper temperaturesMapper;

    @Autowired
    private NowHumidityMapper nowHumidityMapper;

    @Autowired
    private HumiditysMapper humiditysMapper;

    @Autowired
    private NowPositionMapper nowPositionMapper;

    @Autowired
    private RiskMapper riskMapper;

    @Autowired
    private Workshop_deviceIdMapper workshopDeviceIdMapper;

    @Autowired
    private WorkerNumMapper workerNumMapper;

    @Autowired
    private ProductMapper productMapper;
    @Override
    public NowTemperature getNowTemperature(String workshop) {
        if(workshop=="") return null;
        NowTemperature nowTemperature = nowTemperatureMapper.getNowTemperature(workshop);
        return nowTemperature;
    }

    @Override
    public List<NowTemperature> getTemperatureByTime(String workshop, long startTime, long endTime) {
        if(workshop==""||startTime==0||endTime==0) return null;
        List<NowTemperature> tempList = temperaturesMapper.getTemperatureByTime(workshop,startTime,endTime);
        return tempList;
    }

    @Override
    public NowHumidity getNowHumidity(String workshop) {
        if(workshop=="")return null;
        NowHumidity nowHumidity = nowHumidityMapper.getNowHumidity(workshop);
        return nowHumidity;
    }

    @Override
    public List<NowHumidity> getHumidityByTime(String workshop, long startTime, long endTime) {
        if(workshop==""||startTime==0||endTime==0) return null;
        List<NowHumidity> humList = humiditysMapper.getTemperatureByTime(workshop,startTime,endTime);
        return humList;
    }

    @Override
    public List<NowTemperature> getTemperatureByPage(String workshop, int startNumber, int endNumber) {
        if(workshop==""||endNumber==0)return null;
        List<NowTemperature> tempList = temperaturesMapper.getTemperatureByPage(workshop,startNumber,endNumber);
        return tempList;
    }

    @Override
    public List<NowHumidity> getHumidityByPage(String workshop, int startNumber, int endNumber) {
        if(workshop==""||endNumber==0)return null;
        List<NowHumidity> humList = humiditysMapper.getHumidityByPage(workshop,startNumber,endNumber);
        return humList;
    }

    @Override
    public List<NowPosition> getWorkersIdAndLocation() {
        List<NowPosition> workerList = nowPositionMapper.getNowPosition();
        return workerList;
    }

    @Override
    public List<Employee> getWorkersByWorkshop(String workshop) {
        if(workshop=="")return null;
        List<Employee> workerList = nowPositionMapper.getWorkersByWorkshop(workshop);
        return workerList;
    }

    @Override
    public int getWorkerNumByWorkshop(String workshop) {
        if(workshop=="")return 0;
        return workerNumMapper.getWorkerNumByWorkshop(workshop);
    }

    @Override
    public List<WorkerNum> getAllWorkerNumB() {
        List<WorkerNum> numList = new ArrayList<>();
        WorkerNum workerNum1 = new WorkerNum("1",workerNumMapper.getWorkerNumByWorkshop("1"));
        numList.add(workerNum1);
        WorkerNum workerNum2 = new WorkerNum("2",workerNumMapper.getWorkerNumByWorkshop("2"));
        numList.add(workerNum2);
        WorkerNum workerNum3 = new WorkerNum("3",workerNumMapper.getWorkerNumByWorkshop("3"));
        numList.add(workerNum3);
        WorkerNum workerNum4 = new WorkerNum("4",workerNumMapper.getWorkerNumByWorkshop("4"));
        numList.add(workerNum4);
        return numList;
    }

    @Override
    public List<Risk> getRiskRecord(int startNumber, int endNumber) {
        if(endNumber==0) return null;
        List<Risk> riskList = riskMapper.getRiskByPage(startNumber,endNumber);
        return riskList;
    }

    @Override
    public List<Risk> getRiskRecordByWorkshop(String workshop, int startNumber, int endNumber) {
        if(workshop==""||endNumber==0)return null;
        List<Risk> riskList = riskMapper.getRiskByWorkshop(workshop,startNumber,endNumber);
        return riskList;
    }

    @Override
    public Risk getRiskById(int id) {
        if(id==0)return null;
        Risk risk = riskMapper.getRiskById(id);
        return risk;
    }

    @Override
    public List<Risk> getRiskByWorkshopAndLevel(String workshop, int level) {
        if(workshop==""||level==0)return null;
        List<Risk> riskList = riskMapper.getRiskByWorkshopAndLevel(workshop,level);
        return riskList;
    }

    @Override
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
    public List<Workshop_deviceId> getWorkshop() {
        List<Workshop_deviceId> workshopList = workshopDeviceIdMapper.getWorkshop();
        return workshopList;
    }

    @Override
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
    public int getRiskNumByRiskName(long startTime, long endTime, String riskName) {
        if(startTime==endTime||endTime>startTime)return 0;
        int num = riskMapper.getRiskNumByRiskName(startTime,endTime,riskName);
        return num;
    }

    @Override
    public List<Product> getAllProduct() {
        return productMapper.getAllProduct();
    }
}


