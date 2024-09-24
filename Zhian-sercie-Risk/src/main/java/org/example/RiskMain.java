package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.example.util.FileUtil.uploadFile;

@SpringBootApplication
public class RiskMain {
    public static void main(String[] args) {
//        SpringApplication.run(RiskMain.class,args);
        uploadFile("E:/test/img/send.png","E:/test/documents/origin.png");
    }
}