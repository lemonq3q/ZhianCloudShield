package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
@EnableScheduling
@MapperScan("org.example.dao")
public class ModelMain {
    public static void main(String[] args) {
        SpringApplication.run(ModelMain.class, args);
    }
}