package main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import de.vsy.classes.tictactoe.ServerInfo;
import de.vsy.interfaces.IEcho;
import de.vsy.interfaces.IGame;
import de.vsy.interfaces.IServer;
import gui.Login;
import object.GameClient;


public class Main { //Client
	private static IServer server;
	private static String user;

	public static void main(String[] args) {
		System.out.println("Ich bin dein Client.");
		// Gets host
		String host = Login.getServer();
		System.out.println(host);
        try {
        	// Sets policy
        	String clientPath = System.getProperty("user.dir");
			if(!clientPath.toLowerCase().contains("compiled_jar")){
				clientPath += clientPath.endsWith("\\") ? "compiled_jar\\" : "\\compiled_jar\\";
			}
			else{
				clientPath += clientPath.endsWith("\\") ? "" : "\\";
			}
            System.setProperty("java.security.policy", clientPath + "security.policy");
        	System.setSecurityManager(new SecurityManager());
            // Gets remote registry
        	Registry registry = LocateRegistry.getRegistry(host,0);
        	// Gets remote object Echo
            IEcho stub = (IEcho) registry.lookup("Echo");
            String response = stub.echoThis("Hallo Du da.");
            System.out.println("response: " + response);
            // Gets remote server selected
            System.out.println("List of servers:");
            /*Object[] elts = stub.getServers();
            for (Object serverInfo : elts) {
            	ServerInfo tmp = (ServerInfo)serverInfo;
            	System.out.println("\t" + tmp.Name + " on port " + tmp.Port);
			}*/
            server = (IServer) registry.lookup("Server");
            System.out.println("number of objects found: " + registry.list().length);
            for (String serverName : registry.list()) {
            	System.out.println("\t" + serverName);
			}
            
            // Gets player name
            Scanner scan = new Scanner(System.in);
            user = Login.getUsername();
            // Logs in the player
            try {
            	server.login(user);				
			} catch (RemoteException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				System.exit(0);
			}
            // Gets the GameID for this player
            int id = server.getGameId(user);
            System.out.println("Game id is: " + id);
            // Gets the remote object game for this player
            String reg = "Game"+id;
            System.out.println("Getting " + reg);
            IGame gameServer = (IGame) registry.lookup(reg);
            
            GameClient game = new GameClient(gameServer, user);
            IGame gameStub = (IGame) UnicastRemoteObject.exportObject(game,0);
            server.addClientGame(game.getId(), user, gameStub);
            game.Play();
            
            scan.close();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            System.exit(0);
        }
	}

	public static void logout() throws RemoteException {
		if(server != null && user != null) {
			server.logout(user);
		}
	}
}
