package main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.vsy.interfaces.Echo;


public class Main implements Echo{ //Server
	private static Registry registry;
	
	public static void main(String[] args) {
		System.out.println("Server startet. Wenn es mal programmiert wurde...");
		String host = (args.length < 1) ? null : args[0];
		if(host == null){
			System.out.println("Bitte Adresse des Servers beim Start angeben!");
			System.exit(0);
		}
		System.out.println("Server startet auf Adresse: " + host);
		try {
			System.setSecurityManager(new SecurityManager());
			System.setProperty("java.rmi.server.hostname",host);
			Main obj = new Main();
			Echo stub = (Echo) UnicastRemoteObject.exportObject(obj, 0);

			// Starts the RMI Registry
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT); //Port binden
			// Bind the remote object's stub in the registry
            registry = LocateRegistry.getRegistry();

            registry.bind("Echo", stub);

            System.err.println("Server ready!");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
            System.exit(0);
        }
	}

	@Override
	public String echoThis(String text) throws RemoteException {
		return "Echo " + text;
	}
}
