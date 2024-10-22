package org.example.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig extends RabbitmqNameConfig{
    //交换机定义
    @Bean
    DirectExchange oneNetExchange(){ return new DirectExchange(ONENET_EXCHANGE); }

    @Bean
    DirectExchange detectExchange(){ return new DirectExchange(DETECT_EXCHANGE); }

    @Bean
    DirectExchange logExchange(){return new DirectExchange(LOG_EXCHANGE);}

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

    @Bean
    public Queue logQueue(){
        return new Queue(LOG_QUEUE);
    }

    //绑定定义
    @Bean
    @Qualifier("bindingOneNetExchange")
    public Binding bindingOneNetExchange(@Qualifier("temperatureQueue") Queue temperatureQueue, @Qualifier("oneNetExchange") DirectExchange oneNetExchange) {
        return BindingBuilder.bind(temperatureQueue).to(oneNetExchange).with(TEMP_ROUTING_KEY);
    }

    @Bean
    @Qualifier("bindingHumidity")
    public Binding bindingHumidity(@Qualifier("humidityQueue") Queue humidityQueue, @Qualifier("oneNetExchange") DirectExchange oneNetExchange) {
        return BindingBuilder.bind(humidityQueue).to(oneNetExchange).with(HUM_ROUTING_KEY);
    }

    @Bean
    @Qualifier("bindingOnlineStatus")
    public Binding bindingOnlineStatus(@Qualifier("onlineStatusQueue") Queue onlineStatusQueue, @Qualifier("oneNetExchange") DirectExchange oneNetExchange) {
        return BindingBuilder.bind(onlineStatusQueue).to(oneNetExchange).with(ONLINE_ROUTING_KEY);
    }

    @Bean
    @Qualifier("bindingAttendance")
    public Binding bindingAttendance(@Qualifier("attendanceQueue") Queue attendanceQueue, @Qualifier("oneNetExchange") DirectExchange oneNetExchange) {
        return BindingBuilder.bind(attendanceQueue).to(oneNetExchange).with(ATD_ROUTING_KEY);
    }

    @Bean
    @Qualifier("bindingRisk")
    public Binding bindingRisk(@Qualifier("riskQueue") Queue riskQueue, @Qualifier("detectExchange") DirectExchange detectExchange) {
        return BindingBuilder.bind(riskQueue).to(detectExchange).with(RISK_ROUTING_KEY);
    }

    @Bean
    @Qualifier("bindingLog")
    public Binding bindingLog(@Qualifier("logQueue") Queue logQueue, @Qualifier("logExchange") DirectExchange detectExchange) {
        return BindingBuilder.bind(logQueue).to(detectExchange).with(LOG_ROUTING_KEY);
    }

}
