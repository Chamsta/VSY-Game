package main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import intface.Echo;


public class Main implements Echo{

	public static void main(String[] args) {
		System.out.println("Server startet. Wenn es mal programmiert wurde...");
		try {
			Main obj = new Main();
			Echo stub = (Echo) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Echo", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}

	@Override
	public String echoThis(String text) throws RemoteException {
		return "Echo " + text;
	}
}
