package main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.vsy.interfaces.IEcho;
import de.vsy.interfaces.IServer;
import objects.Echo;
import objects.Server;


public class Main { //Server
	private static final int RMI_PORT_MIN = 2000;
	private static final int RMI_PORT_MAX = 49150;
	private static Registry registry;
	private static int portNumber;
	
	public static void main(String[] args) {
		System.out.println("Server startet. Wenn es mal programmiert wurde...");
		String host = (args.length < 1) ? null : args[0];
		if(host == null){
			System.out.println("Bitte Adresse des Servers beim Start angeben!");
			System.exit(0);
		}
		if(args.length >= 2){
			try {
				portNumber = Integer.parseInt(args[1]);
				if(portNumber < RMI_PORT_MIN && portNumber > RMI_PORT_MAX){
					portNumber = RMI_PORT_MIN;
				}
			} catch (NumberFormatException e) {
				portNumber = RMI_PORT_MIN;
			}
		}
		else{
			portNumber = RMI_PORT_MIN;
		}
		boolean loggoutAllUsers = (args.length < 3) ? false : Boolean.valueOf(args[2]);
		System.out.println("Server startet auf Adresse: " + host);
		try {
			String serverPath = System.getProperty("user.dir") + "\\compiled_jar\\";
			if(!serverPath.toLowerCase().contains("compiled_jar")){
				serverPath += serverPath.endsWith("\\") ? "compiled_jar\\" : "\\compiled_jar\\";
			}
        	//Beim Starten aus Eclipse den vollständigen Pfad zu security.policy eingeben!
			System.setProperty("java.security.policy", serverPath + "security.policy");
			System.setSecurityManager(new SecurityManager());
			System.setProperty("java.rmi.server.hostname",host);
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT); 
			registry = LocateRegistry.getRegistry();
			
			IEcho objEcho = new Echo();
			IEcho stubEcho = (IEcho) UnicastRemoteObject.exportObject(objEcho, portNumber);
			
			Server server = new Server(registry, portNumber);
			if(loggoutAllUsers) {
				Server.dbConnection.logoutAllUsers();
				System.out.println("Alle User ausgeloggt.");
			}
			IServer serverStub = (IServer) UnicastRemoteObject.exportObject(server, portNumber);

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
