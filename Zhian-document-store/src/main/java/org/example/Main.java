package org.example;

<<<<<<< HEAD
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
=======
import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) {
        new FileServer();
>>>>>>> cc6716667139555734c73a3bb2d974365344dbbd
    }
}