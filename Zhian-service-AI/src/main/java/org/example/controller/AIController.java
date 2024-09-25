package org.example.controller;

import org.example.resp.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

@RestController
public class AIController {

    @PostMapping("/AI/detectTest")
    public ResultData PictureDetect(@RequestParam("file") MultipartFile file,@RequestParam("name") String name,@RequestParam("model") Integer id){
        String picName = file.getName().split(".")[1];
        String PATH = "..."+picName;
        try {
            File filePath = new File (PATH);
            file.transferTo(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @PostMapping("/AI/imgSave")
    public ResultData ImgSave(@RequestParam("file")MultipartFile file,@RequestParam("name") String picName){
        return null;
    }
}
