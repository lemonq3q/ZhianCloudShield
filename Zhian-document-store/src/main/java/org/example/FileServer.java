package org.example;

import org.example.util.FileDataService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class FileServer {

    FileDataService fileDataService;

    public FileServer(){
        try{
            fileDataService = new FileDataService_impl();
            LocateRegistry.createRegistry(9001);
            Naming.rebind("rmi://localhost:9001/FileDataService",fileDataService);
        }catch (RemoteException e){
            e.printStackTrace();
        }catch( MalformedURLException e){
            e.printStackTrace();
        }
    }
}