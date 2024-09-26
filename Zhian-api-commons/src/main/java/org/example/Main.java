package org.example;
System.out.println("Hello world!");

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.example.util.FileUtil.uploadFile;
import static org.example.util.FileUtil.downloadFile;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
//        uploadFile("E:/test/img/send.png","E:/test/documents/origin.png");
//        downloadFile("E:/test/documents/send_get.png","send.png");
        SpringApplication.run(Main.class, args);

    }
}