package org.example.service.impl;

import org.example.config.RabbitmqNameConfig;
import org.example.resp.ResultData;
import org.example.service.AIService;
import org.example.util.FileUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Component
public class AIServiceImpl implements AIService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    private final static String IMAGE = "image";
    private final static String MODEL = "model";
    private final static String TMP_SAVE_PATH = "E:/CodeTest/file_save/tmp_image";

    @Override
    public ResultData PictureDetect(MultipartFile file, String name, Integer id) {
        try {
            byte[] imageData = file.getBytes();
            MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setHeader("image-name",name);
                    return message;
                }
            };
            rabbitTemplate.convertAndSend(RabbitmqNameConfig.DETECT_EXCHANGE,"Model"+id,imageData,messagePostProcessor);
        } catch (IOException e) {
            return ResultData.fail("500","image read failed");
        }
        return ResultData.success("start detect");
    }

    //上传文件实现
    @Override
    public ResultData updateFile(MultipartFile file, String name, String type) {
        try {
            System.out.println(TMP_SAVE_PATH+"/"+name);
            file.transferTo(new File(TMP_SAVE_PATH+"/"+name));
            if(type.equals(IMAGE)){
                FileUtil.uploadFile(FileUtil.IMAGE_SAVE_PATH+"/"+name,TMP_SAVE_PATH+"/"+name);
            }
            else if(type.equals(MODEL)){
                FileUtil.uploadFile(FileUtil.MODEL_SAVE_PATH+"/"+name,TMP_SAVE_PATH+"/"+name);
            }
        } catch (IOException e) {
            return ResultData.fail("500","file save failed");
        }
        return ResultData.success("file upload succeed");
    }

    @Override
    public ResultData getDetectPicUrl(String name){
        long delayTime = 1000 * 30;
        final boolean[] yes_result = {false};
        final boolean[] no_result = {false};

        FileUtil.isExist(FileUtil.IMAGE_SAVE_PATH+ "/" + name,delayTime);

        Thread thread1 = new Thread(() -> {

            yes_result[0] = FileUtil.isExist(FileUtil.IMAGE_SAVE_PATH+"/"+name.split(".")[0]+"_yes"+name.split(".")[1],delayTime);
            interruptThreadByName("thread2");
        },"thread1");

        Thread thread2 = new Thread(() -> {
            no_result[0] = FileUtil.isExist(FileUtil.IMAGE_SAVE_PATH+"/"+name.split(".")[0]+"_no"+name.split(".")[1],delayTime);
            interruptThreadByName("thread1");
        },"thread2");

        thread1.start();
        thread2.start();

        try {
            thread1.join(); // 等待线程1执行完毕
            thread2.join(); // 等待线程2执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(yes_result[0]){
            return ResultData.success("http://"+ FileUtil.FILE_URL +":80/image/" + name);
        }
        else{
            return ResultData.fail("500","get failed");
        }
    }

    private static void interruptThreadByName(String name) {
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[currentGroup.activeCount()];
        currentGroup.enumerate(threads);

        for (Thread thread : threads) {
            if (thread != null && thread.getName().equals(name)) {
                thread.interrupt();
                break;
            }
        }
    }
}
