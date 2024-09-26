package org.example;

<<<<<<< HEAD
<<<<<<< HEAD
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
=======
=======
>>>>>>> cc6716667139555734c73a3bb2d974365344dbbd
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
<<<<<<< HEAD
>>>>>>> cc6716667139555734c73a3bb2d974365344dbbd
=======
>>>>>>> cc6716667139555734c73a3bb2d974365344dbbd
    }
}