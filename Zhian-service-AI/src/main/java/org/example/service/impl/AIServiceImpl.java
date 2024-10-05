package org.example.service.impl;

import org.example.config.RabbitmqConfig;
import org.example.resp.ResultData;
import org.example.service.AIService;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Component
public class AIServiceImpl implements AIService {

    @Autowired
    RabbitTemplate rabbitTemplate;

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
}
