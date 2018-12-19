package main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import dbconnect.DBConnection;
import de.vsy.interfaces.Echo;
import de.vsy.interfaces.ServerInterface;
import objects.Server;


public class Main implements Echo{ //Server
	private static Registry registry;
	
	public static void main(String[] args) {
		System.out.println("Server startet. Wenn es mal programmiert wurde...");
		String host = (args.length < 1) ? null : args[0];
		if(host == null){
			System.out.println("Bitte Adresse des Servers beim Start angeben!");
			System.exit(0);
		}
		boolean loggoutAllUsers = (args.length < 2) ? false : Boolean.valueOf(args[0]);
		System.out.println("Server startet auf Adresse: " + host);
		try {
        	//Beim Starten aus Eclipse den vollständigen Pfad zu security.policy eingeben!
			System.setProperty("java.security.policy", "security.policy");
			System.setSecurityManager(new SecurityManager());
			System.setProperty("java.rmi.server.hostname",host);
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT); 
			registry = LocateRegistry.getRegistry();
			
			Main obj = new Main();
			Echo stub = (Echo) UnicastRemoteObject.exportObject(obj,0);
			
			Server server = new Server(registry);
			if(loggoutAllUsers) {
				Server.dbConnection.logoutAllUsers();
				System.out.println("Alle User ausgeloggt.");
			}
			ServerInterface serverStub = (ServerInterface) UnicastRemoteObject.exportObject(server,0);

            registry.bind("Echo", stub);
            registry.bind("Server", serverStub);
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

	@Override
	public String sqlQuery(String query) throws RemoteException {
		DBConnection dbConnection = new DBConnection();
		String test = dbConnection.execute(query);
		return test;
	}
}
