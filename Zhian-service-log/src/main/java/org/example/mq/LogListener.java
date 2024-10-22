package org.example.mq;


import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import org.example.annotation.Master;
import org.example.config.RabbitmqNameConfig;
import org.example.entity.AdminLog;
import org.example.mapper.AdminLogMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LogListener extends RabbitmqNameConfig {

    @Resource
    AdminLogMapper adminLogMapper;

    @Master
    @RabbitListener(queues = LOG_QUEUE)
    public void logProcess(Message message, Channel channel) {
        String jsonString = new String(message.getBody());
        AdminLog adminLog = JSON.parseObject(jsonString, AdminLog.class);
        adminLogMapper.insertAdminLog(adminLog);
    }
}
