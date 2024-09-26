package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

public class RabbitmqConfig {
    public static final String ONENET_EXCHANGE = "oneNetExchange";
    public static final String DETECT_EXCHANGE = "detectExchange";

    public static final String TEMP_QUEUE = "temperatureQueue";
    public static final String HUM_QUEUE = "humidityQueue";
    public static final String ATD_QUEUE = "attendanceQueue";
    public static final String ONLINE_QUEUE = "onlineStatusQueue";
    public static final String RISK_QUEUE = "riskQueue";

    public static final String TEMP_ROUTING_KEY = "temperature";
    public static final String HUM_ROUTING_KEY = "humidity";
    public static final String ATD_ROUTING_KEY = "attendance";
    public static final String ONLINE_ROUTING_KEY = "onlineStatus";
    public static final String RISK_ROUTING_KEY = "risk";

    //交换机定义
    @Bean
    DirectExchange oneNetExchange(){ return new DirectExchange(ONENET_EXCHANGE); }

    @Bean
    DirectExchange detectExchange(){ return new DirectExchange(DETECT_EXCHANGE); }

    //队列定义
    @Bean
    public Queue temperatureQueue(){
        return new Queue(TEMP_QUEUE);
    }

    @Bean
    public Queue humidityQueue(){
        return new Queue(HUM_QUEUE);
    }

    @Bean
    public Queue attendanceQueue(){
        return new Queue(ATD_QUEUE);
    }

    @Bean
    public Queue onlineStatusQueue(){
        return new Queue(ONLINE_QUEUE);
    }

    @Bean
    public Queue riskQueue(){
        return new Queue(RISK_QUEUE);
    }

    //绑定定义
    @Bean
    public Binding bindingOneNetExchange(Queue temperatureQueue, DirectExchange oneNetExchange) {
        return BindingBuilder.bind(temperatureQueue).to(oneNetExchange).with(TEMP_ROUTING_KEY);
    }

    @Bean
    public Binding bindingHumidity(Queue humidityQueue, DirectExchange oneNetExchange) {
        return BindingBuilder.bind(humidityQueue).to(oneNetExchange).with(HUM_ROUTING_KEY);
    }

    @Bean
    public Binding bindingOnlineStatus(Queue onlineStatusQueue, DirectExchange oneNetExchange) {
        return BindingBuilder.bind(onlineStatusQueue).to(oneNetExchange).with(ONLINE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingAttendance(Queue attendanceQueue, DirectExchange oneNetExchange) {
        return BindingBuilder.bind(attendanceQueue).to(oneNetExchange).with(ATD_ROUTING_KEY);
    }

    @Bean
    public Binding bindingRisk(Queue riskQueue, DirectExchange detectExchange) {
        return BindingBuilder.bind(riskQueue).to(detectExchange).with(RISK_ROUTING_KEY);
    }

}
