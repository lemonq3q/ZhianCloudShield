package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD

@SpringBootApplication
=======
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
>>>>>>> cc6716667139555734c73a3bb2d974365344dbbd
public class LoginMain {
    public static void main(String[] args) {
        SpringApplication.run(LoginMain.class,args);
    }
}