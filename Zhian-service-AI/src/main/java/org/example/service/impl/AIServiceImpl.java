package org.example.service.impl;

import org.example.config.RabbitmqConfig;
import org.example.resp.ResultData;
import org.example.service.AIService;
import org.example.util.FileUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Component
public class AIServiceImpl implements AIService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    private final static String IMAGE = "image";
    private final static String MODEL = "model";
    private final static String TMP_SAVE_PATH = "****";

    @Override
    public ResultData PictureDetect(MultipartFile file, String name, Integer id) {
        try {
            byte[] imageData = file.getBytes();
            MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setHeader("image-name",name);
                    return message;
                }
            };
            rabbitTemplate.convertAndSend(RabbitmqConfig.DETECT_EXCHANGE,"Model"+id,imageData,messagePostProcessor);
        } catch (IOException e) {
            return ResultData.fail("500","image read failed");
        }
        return ResultData.success("start detect");
    }

    //上传文件实现
    @Override
    public ResultData updateFile(MultipartFile file, String name, String type) {
        try {
            file.transferTo(new File(TMP_SAVE_PATH+name));
            if(type.equals(IMAGE)){
                FileUtil.uploadFile(FileUtil.IMAGE_SAVE_PATH+name,TMP_SAVE_PATH+name);
            }
            else if(type.equals(MODEL)){
                FileUtil.uploadFile(FileUtil.MODEL_SAVE_PATH+name,TMP_SAVE_PATH+name);
            }
        } catch (IOException e) {
            return ResultData.fail("500","file save failed");
        }
        return ResultData.success("file upload succeed");
    }
}
