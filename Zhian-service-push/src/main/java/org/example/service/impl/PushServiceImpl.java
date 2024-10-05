package org.example.service.impl;

import com.alibaba.fastjson.JSON;
import org.example.entity.NowPosition;
import org.example.entity.WebSocketMessage;
import org.example.resp.ResultData;
import org.example.resp.ReturnCodeEnum;
import org.example.service.PushService;
import org.example.websocket.RiskEndpoint;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PushServiceImpl implements PushService {

    @Override
    public ResultData<String> AttendancePush(NowPosition sendMessage) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(1,sendMessage);
        //发送信息的代码块
        Set<String> keys = RiskEndpoint.onlineUsers.keySet();
        for (String key : keys) {
            try {
                RiskEndpoint.onlineUsers.get(key).getBasicRemote().sendText(JSON.toJSONString(webSocketMessage)); //转换为json
            }catch (Exception e){
                return ResultData.fail(ReturnCodeEnum.RC500.getCode(), "send failed");
            }
        }
        return ResultData.success("send success");
    }
}
