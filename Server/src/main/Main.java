package main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.vsy.interfaces.IEcho;
import de.vsy.interfaces.ServerInterface;
import objects.Echo;
import objects.Server;


public class Main { //Server
	private static Registry registry;
	private static int portNumber;
	
	public static void main(String[] args) {
		System.out.println("Server startet. Wenn es mal programmiert wurde...");
		String host = (args.length < 1) ? null : args[0];
		if(host == null){
			System.out.println("Bitte Adresse des Servers beim Start angeben!");
			System.exit(0);
		}
		if(args.length == 2){
			try {
				portNumber = Integer.parseInt(args[1]);
				if(portNumber < 2000 && portNumber > 49150){
					portNumber = 2000;
				}
			} catch (NumberFormatException e) {
				portNumber = 2000;
			}
		}
		else{
			portNumber = 2000;
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
			
			IEcho objEcho = new Echo();
			IEcho stubEcho = (IEcho) UnicastRemoteObject.exportObject(objEcho, portNumber);
			
			Server server = new Server(registry);
			if(loggoutAllUsers) {
				Server.dbConnection.logoutAllUsers();
				System.out.println("Alle User ausgeloggt.");
			}
			ServerInterface serverStub = (ServerInterface) UnicastRemoteObject.exportObject(server, portNumber);

            registry.bind("Echo", stubEcho);
            registry.bind("Server", serverStub);
            System.err.println("Server ready!");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
            System.exit(0);
        }
	}
}
