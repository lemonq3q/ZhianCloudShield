package org.example;

import static org.example.util.FileUtil.uploadFile;
import static org.example.util.FileUtil.downloadFile;

public class Main {
    public static void main(String[] args) {
        uploadFile("E:/test/img/send.png","E:/test/documents/origin.png");
        downloadFile("E:/test/documents/send_get.png","send.png");
    }
}