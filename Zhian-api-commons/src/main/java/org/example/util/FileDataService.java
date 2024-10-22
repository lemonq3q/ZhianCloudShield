package org.example.util;

import javax.swing.text.StyledEditorKit;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileDataService extends Remote {
    void upload(String filename,byte[] file) throws RemoteException;

    byte[] getFile(String filePath) throws RemoteException;

    Boolean isExist(String filePath, long delayTime) throws RemoteException;
}
