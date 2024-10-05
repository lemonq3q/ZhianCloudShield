package org.example.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import org.example.config.RabbitmqConfig;
import org.example.entity.Risk;
import org.example.websocket.RiskEndpoint;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class RiskListener extends RabbitmqConfig {

    @RabbitListener(queues = {RISK_QUEUE})
    public void riskPush(Message message, Channel channel) throws Exception{
        String jsonString = new String(message.getBody());
        for(String userid: RiskEndpoint.onlineUsers.keySet()){
            RiskEndpoint.onlineUsers.get(userid).getBasicRemote().sendText(jsonString);
        }
    }
}
