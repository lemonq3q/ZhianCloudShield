package org.example.aop;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.annotation.SystemControllerLog;
import org.example.config.RabbitmqNameConfig;
import org.example.entity.AdminLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Aspect
@Component
public class SystemLogAspect {
    private static Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Pointcut("@annotation(org.example.annotation.SystemControllerLog)")
    public void logPointCut(){

    }


    @Around(value = "logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint){
        logger.info("调用日志监控");
        AdminLog adminLog = new AdminLog();
        adminLog.setOperateTimestamp(System.currentTimeMillis());
        /*从切面值入点获取植入点方法*/
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        /*获取切入点方法*/
        Method method = signature.getMethod();
        /*获取方法上的值*/
        SystemControllerLog systemControllerLog = method.getAnnotation(SystemControllerLog.class);
        /*保存操作事件*/
        if (systemControllerLog != null) {
            String operation = systemControllerLog.operation();

            adminLog.setOperation(operation);
            /*保存日志类型*/
            adminLog.setOperation(operation);
            String type = systemControllerLog.type();
            adminLog.setType(type);
            /*打印*/
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUri = request.getRequestURI();/*获取请求地址*/
        String requestMethod = request.getMethod();/*获取请求方式*/
        String remoteAddr1 = request.getRemoteAddr();/*获取请求IP*/
        String remoteAddr = this.getIpAddress(request);
        /*存请求地址，请求方式，请求IP*/
        adminLog.setRemoteAddr(remoteAddr);
        //logger.info("客户端IP为：" + remoteAddr);
        adminLog.setRequestUri(requestUri);
        logger.info("请求路径为：" + requestUri);
        adminLog.setMethod(requestMethod);
        logger.info("请求方式为：" + requestMethod);
        /*获取参数*/
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (Object obj : args) {
                /*   System.out.println("传递的参数" + obj);*/
                String params = obj.toString();
                /*      System.out.println("传递的参数" + params);*/
                logger.info("请求参数为：" + params);
                /*保存请求参数*/
                adminLog.setParams(params);
            }
        }
        Object proceed = null;

        try {
            //执行增强后的方法
            proceed = joinPoint.proceed();
            if (method.isAnnotationPresent(SystemControllerLog.class)) {
                adminLog.setExceptionLog("无异常");
                adminLog.setType("info");
                adminLog.setResultParams(proceed.toString());

            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            adminLog.setExceptionLog(throwable.getMessage());
            adminLog.setType("Err");
            adminLog.setResultParams(proceed.toString());
        } finally {
            //发送log信息到队列上
            sendLog(adminLog);
        }
        logger.info("返回参数为"+proceed);
        return proceed;
    }

    public String getIpAddress(HttpServletRequest request) {

        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    public void sendLog(AdminLog adminLog){
        String sendMessage = JSON.toJSONString(adminLog);
        rabbitTemplate.convertAndSend(RabbitmqNameConfig.LOG_EXCHANGE, RabbitmqNameConfig.LOG_ROUTING_KEY, sendMessage);
    }
}
