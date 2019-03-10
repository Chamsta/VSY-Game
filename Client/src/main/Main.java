package main;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import javax.swing.JOptionPane;

import de.vsy.classes.tictactoe.Helper;
import de.vsy.interfaces.IGame;
import de.vsy.interfaces.IServer;
import gui.Login;
import object.GameClient;
import object.ServerFinder;


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
			clientPath = Helper.separatorsToSystem(clientPath);
            System.setProperty("java.security.policy", clientPath + "security.policy");
        	System.setSecurityManager(new SecurityManager());
            // Gets remote server selected
            //System.out.println("List of servers:");
            /*Object[] elts = stub.getServers();
            for (Object serverInfo : elts) {
            	ServerInfo tmp = (ServerInfo)serverInfo;
            	System.out.println("\t" + tmp.Name + " on port " + tmp.Port);
			}*/
            server = ServerFinder.getServer(host);
            Registry registry = ServerFinder.getRegistry(host);
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
           
            IGame gameServer = ServerFinder.getGameServer(user);
            
            GameClient game = new GameClient(gameServer, user);
            IGame gameStub = (IGame) UnicastRemoteObject.exportObject(game,0);
            server.addClientGame(game.getId(), user, gameStub);
            game.Play();
            
            scan.close();
        } catch (Exception e) {
        	if(server != null)
				try {
					server.logout(user);
				} catch (RemoteException e1) {
				}
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            System.exit(0);
        }
	}

	public static void logout() throws Exception {
		server = ServerFinder.getServer(null);
		if(server != null && user != null) {
			try {
				server.logout(user);				
			} catch (ConnectException e) {
				server = ServerFinder.getServer(null);
				server.logout(user);
			}
		}
	}
}
