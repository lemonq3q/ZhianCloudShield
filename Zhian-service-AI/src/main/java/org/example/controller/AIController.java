package org.example.controller;

import org.example.resp.ResultData;
import org.example.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/ai/detectTest")
    public ResultData PictureDetect(@RequestParam("file") MultipartFile file,@RequestParam("name") String name,@RequestParam("model") Integer id){
        return aiService.PictureDetect(file,name,id);
    }

    @PostMapping("/ai/updateFile")
    public ResultData updateFile(@RequestParam("file") MultipartFile file,@RequestParam("name") String name,@RequestParam("type") String type){
    	return aiService.updateFile(file,name,type);
    }

    @GetMapping("/ai/getFileUrl")
    public ResultData getFileUrl(String name){
        return aiService.getDetectPicUrl(name);
    }

}
