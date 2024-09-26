package org.example.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileDataService extends Remote {
    void upload(String filename,byte[] file) throws RemoteException;

    byte[] getFile(String filename) throws RemoteException;
}
