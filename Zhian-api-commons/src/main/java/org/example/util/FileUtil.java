package org.example.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Iterator;

public class FileUtil {

    public final static String IMAGE_SAVE_PATH = "E:\\CodeTest\\file_save\\image";

    public final static String MODEL_SAVE_PATH = "E:\\CodeTest\\file_save\\model";

    public static void main(String[] args) {
        try {
            String inPath = "E:/CodeTest/user6.png";
            String filePath = "E:/CodeTest/test.jpg";
            String filename = "test.png";
            File file = new File(inPath);//
            InputStream inputStream = new FileInputStream(file);//
            BufferedImage originalImage = ImageIO.read(inputStream);

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            int squareSize = Math.min(width, height);
            int x = (width - squareSize) / 2;
            int y = (height - squareSize) / 2;
            BufferedImage croppedImage = originalImage.getSubimage(x, y, squareSize, squareSize);

            File outputFile = new File(filePath);

            int dotIndex = filename.lastIndexOf(".");
            String format = filename.substring(dotIndex + 1);

            System.out.println(croppedImage);
            ImageIO.write(croppedImage, format, outputFile);
        }catch (Exception e){

        }
    }
    /**
     * 解压zip文件
     * @param multipartFile
     * @param outDir
     * @param tmpName
     * @return
     */
    public static boolean unZip(MultipartFile multipartFile,String outDir,String tmpName){
        try {
            Path tempFilePath = Files.createTempFile(tmpName,".zip");
            InputStream inputStream = multipartFile.getInputStream();
            Files.copy(inputStream,tempFilePath, StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();
            ZipFile zip = new ZipFile(tempFilePath.toFile());
            File outFileDir = new File(outDir);
            if(!outFileDir.exists()){
                outFileDir.mkdirs();
            }
            for(Enumeration enumeration = zip.getEntries(); enumeration.hasMoreElements();){
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                String zipEntryName = entry.getName();
                int index;
                if(zipEntryName.indexOf("/")!=-1 && zipEntryName.indexOf("\\")!=-1){
                    index  = Math.min(zipEntryName.indexOf("/"), zipEntryName.indexOf("\\"));
                }else{
                    index = Math.max(zipEntryName.indexOf("/"), zipEntryName.indexOf("\\"));
                }
                if (index!=-1) {
                    zipEntryName = zipEntryName.substring(index + 1);
                }
                InputStream in = zip.getInputStream(entry);
                if (entry.isDirectory()) {
                    File fileDir = new File(outDir,zipEntryName);
                    fileDir.mkdir();
                    continue;
                }
                File file = new File(outDir, zipEntryName);
                file.createNewFile();
                OutputStream out = new FileOutputStream(file);
                byte[] buff = new byte[1024];
                int len;
                while ((len = in.read(buff)) > 0) {
                    out.write(buff, 0, len);
                }
                in.close();
                out.close();
            }
            try {
//                Files.delete(tempFilePath);
                tempFilePath.toFile().deleteOnExit();
            } catch (Exception e) {
                System.err.println("Error deleting temporary file: " + tempFilePath);
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }

        return true;
    }

    /**
     * 把图像裁剪成正方形
     * @param picPath
     * @param readImageFormat
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static boolean cropImage(String picPath,String readImageFormat,int x,int y,int width,int height){
        FileInputStream fis = null;
        ImageInputStream iis = null;
        try {
            //创建图片输入流
            fis = new FileInputStream(picPath);
            iis = ImageIO.createImageInputStream(fis);
            //构造图片reader
            Iterator it = ImageIO.getImageReadersByFormatName(readImageFormat);
            ImageReader imageReader = (ImageReader) it.next();
            imageReader.setInput(iis, true);
            //构造图片处理参数
            ImageReadParam param = imageReader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            //通过参数过滤图像，实现裁剪
            BufferedImage bi = imageReader.read(0, param);
            ImageIO.write(bi, readImageFormat, new File(picPath));
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    /**
     * 级联删除目录
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath){
        return true;
    }


    public static byte[] fileToByte(String filename){
        byte[] b = null;
        try{
            File file = new File(filename);
            b = new byte[(int)file.length()];
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
            is.read(b);
            is.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return b;
    }


    public static boolean uploadFile(String savePath,String filePath){
        try {
            FileDataService fileDataService = (FileDataService) Naming.lookup("rmi://26.18.243.179:9001/FileDataService");
            fileDataService.upload(savePath, fileToByte(filePath));
            return true;
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean downloadFile(String savePath,String filePath){
        try {
            FileDataService fileDataService = (FileDataService) Naming.lookup("rmi://26.18.243.179:9001/FileDataService");
            byte[] fileData = fileDataService.getFile(filePath);
            FileOutputStream fos = new FileOutputStream(savePath);
            fos.write(fileData);
            fos.close();
            return true;
        } catch (NotBoundException | IOException e) {
            e.printStackTrace();
            return false;
       }
    }


}
