package org.example.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.example.api.RiskAPi;
import org.example.config.RabbitmqConfig;
import org.example.entity.Risk;
import org.example.websocket.RiskEndpoint;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RiskListener extends RabbitmqConfig {

    @Autowired
    private RiskAPi riskAPi;

    @RabbitListener(queues = {RISK_QUEUE})
    public void riskPush(Message message, Channel channel) throws Exception{
        String jsonString = new String(message.getBody());
        for(String userid: RiskEndpoint.onlineUsers.keySet()){
            RiskEndpoint.onlineUsers.get(userid).getBasicRemote().sendText(jsonString);
        }
        riskAPi.addRisk(JSON.parseObject(jsonString, Risk.class));
    }
}
