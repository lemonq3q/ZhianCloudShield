package org.example.entity;

import lombok.Data;

@Data
public class AdminLog {
    private Integer logId;                   //日志主键
    private String type;                     //日志类型
    private String operation;                 //日志操作事件描述
    private String remoteAddr;                //请求地址ip
    private String requestUri;                //URI
    private String method;                   //请求方式
    private String params;                   //提交参数
    private long operateTimestamp;                    //开始时间
    private Integer userId;                    //用户ID
    private String userName;                 //用户名称
    private String resultParams;            //返回参数
    private String exceptionLog;           //异常描述
}
