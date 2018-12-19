package main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import javax.swing.JOptionPane;

import de.vsy.interfaces.Echo;
import de.vsy.interfaces.GameInterface;
import de.vsy.interfaces.ServerInterface;
import gui.Login;
import object.GameClient;


public class Main { //Client
	private static ServerInterface server;
	private static String user;

	public static void main(String[] args) {
		System.out.println("Ich bin dein Client.");
		String host = Login.getServer();
		System.out.println(host);
        try {
        	//Beim Starten aus Eclipse den vollständigen Pfad zu security.policy eingeben!
//        	System.setProperty("java.security.policy", "security.policy");
        	System.setSecurityManager(new SecurityManager());
            Registry registry = LocateRegistry.getRegistry(host);
            Echo stub = (Echo) registry.lookup("Echo");
            String response = stub.echoThis("Hallo Du da.");
            System.out.println("response: " + response);
            
            server = (ServerInterface) registry.lookup("Server");
            
            Scanner scan = new Scanner(System.in);
            user = Login.getUsername();
            try {
            	server.login(user);				
			} catch (RemoteException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				System.exit(0);
			}
            int id = server.getGameId(user);
            System.out.println("Game id is: " + id);
            
            String reg = "Game"+id;
            System.out.println("Getting " + reg);
            GameInterface gameServer = (GameInterface) registry.lookup(reg);
            
            GameClient game = new GameClient(gameServer, user);
            GameInterface gameStub = (GameInterface) UnicastRemoteObject.exportObject(game,0);
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
