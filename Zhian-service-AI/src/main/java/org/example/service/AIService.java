package org.example.service;

import org.example.resp.ResultData;
import org.springframework.web.multipart.MultipartFile;

public interface AIService {
    public ResultData PictureDetect(MultipartFile file, String name,Integer id);

    public ResultData updateFile(MultipartFile file,String name,String type);

    public ResultData getDetectPicUrl(String name);
}
