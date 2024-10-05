package org.example;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.example.util.FileDataService;
import org.example.util.FileUtil;

public class FileDataService_impl extends UnicastRemoteObject implements FileDataService {

    public FileDataService_impl() throws RemoteException{

    }

    @Override
    public void upload(String filename, byte[] fileData) throws RemoteException {
        File file = new File(filename);
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            os.write(fileData);
            System.out.println("文件上传成功");
            os.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getFile(String filePath) throws RemoteException {
        return FileUtil.fileToByte(filePath);
    }
}
