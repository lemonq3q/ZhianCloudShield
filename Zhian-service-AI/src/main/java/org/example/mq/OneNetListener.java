package org.example.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import org.example.api.ModelApi;
import org.example.config.RabbitmqConfig;
import org.example.entity.Model;
import org.example.util.FileUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

@Component
public class OneNetListener extends RabbitmqConfig {

    @Resource
    ModelApi modelApi;

    private final static String LOCAL_MODEL_PATH = "****";

    @RabbitListener(queues = {TEMP_QUEUE})
    public void deviceOnline(Message message, Channel channel){
        String jsonString = new String(message.getBody());
        Map<String, String> dataMap = JSON.parseObject(jsonString, Map.class);
        String dev_id = dataMap.get("dev_id");
        List<Model> modelList = (List<Model>) modelApi.getModelByDevice(dev_id).getData();
        //模型文件下载到本地
        for(Model model:modelList){
            File file = new File(LOCAL_MODEL_PATH+model.getId());
            if(!file.exists()){
                FileUtil.downloadFile(LOCAL_MODEL_PATH+model.getId(),model.getModelPath());
            }
        }
        startDetect(dev_id,modelList);
    }

    private void startDetect(String dev_id,List<Model> modelList){

    }
}
