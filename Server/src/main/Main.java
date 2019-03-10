package main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.vsy.classes.tictactoe.ServerInfo;
import de.vsy.interfaces.IEcho;
import de.vsy.interfaces.IServer;
import objects.Echo;
import objects.Server;


public class Main { //Server
	public static final int RMI_PORT_MIN = 2000;
	private static final int RMI_PORT_MAX = 49150;
	private static int portNumber;
	
	public static void main(String[] args) {
		System.out.println("Server starting...");
		String host = (args.length < 1) ? null : args[0];
		if(host == null){
			System.out.println("Bitte Adresse des Servers beim Start angeben!");
			System.exit(0);
		}
		if(args.length >= 2){
			try {
				portNumber = Integer.parseInt(args[1]);
				if(portNumber < RMI_PORT_MIN || portNumber > RMI_PORT_MAX){
					portNumber = Server.getFreePort(host);
				}
			} catch (NumberFormatException e) {
				portNumber = Server.getFreePort(host);
			}
		}
		else{
			portNumber = Server.getFreePort(host);
		}
		boolean loggoutAllUsers = (args.length < 3) ? true : Boolean.valueOf(args[2]);
		System.out.println("Server startet auf Adresse: " + host + ":" + portNumber);
		Server server = null;
		try {
			String serverPath = System.getProperty("user.dir");
			if(!serverPath.toLowerCase().contains("compiled_jar")){
				serverPath += serverPath.endsWith("\\") ? "compiled_jar\\" : "\\compiled_jar\\";
			}
			else{
				serverPath += serverPath.endsWith("\\") ? "" : "\\";
			}
        	//Beim Starten aus Eclipse den vollständigen Pfad zu security.policy eingeben!
			System.setProperty("java.security.policy", serverPath + "security.policy");
			System.setSecurityManager(new SecurityManager());
			System.setProperty("java.rmi.server.hostname",host);
			System.setProperty("java.rmi.server.logCalls", "true");
			
			Registry registry = LocateRegistry.createRegistry(portNumber);
			
			Echo objEcho = new Echo();
			objEcho.addServer(new ServerInfo("Server", portNumber));
			IEcho stubEcho = (IEcho) UnicastRemoteObject.exportObject(objEcho, portNumber);
			
			server = new Server(host, portNumber);
			server.checkServers();
			if(loggoutAllUsers) {
				Server.dbConnection.logoutAllUsers();
				System.out.println("Alle User ausgeloggt.");
			}
			IServer serverStub = (IServer) UnicastRemoteObject.exportObject(server, portNumber);
            
			registry.bind("Echo", stubEcho);
            registry.bind("Server", serverStub);
            System.err.println("Server ready!");
        } catch (Exception e) {
        	if(server != null) {
				try {
					server.logout();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
            System.exit(0);
        }
	}
}
