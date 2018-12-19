package main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Scanner;


import de.vsy.interfaces.Echo;
import de.vsy.interfaces.GameInterface;
import de.vsy.interfaces.ServerInterface;
import gui.Login;
import object.Game;


public class Main { //Client

	public static void main(String[] args) {
		System.out.println("Ich bin dein Client.");
		String host = Login.getServer();
		System.out.println(host);
        try {
        	//Beim Starten aus Eclipse den vollstaendigen Pfad zu security.policy eingeben!
        	System.setProperty("java.security.policy", "security.policy");
        	System.setSecurityManager(new SecurityManager());
            Registry registry = LocateRegistry.getRegistry(host);
            Echo stub = (Echo) registry.lookup("Echo");
            String response = stub.echoThis("Hallo Du da.");
            System.out.println("response: " + response);
            
            ServerInterface server = (ServerInterface) registry.lookup("Server");
            
            Scanner scan = new Scanner(System.in);
            String user = Login.getUsername();
            server.login(user);
            int id = server.getGameId(user);
            System.out.println("Game id is: " + id);
            
            String reg = "Game"+id;
            System.out.println("Getting " + reg);
            GameInterface gameServer = (GameInterface) registry.lookup(reg);
            HashMap<String, Boolean> cells = gameServer.getCells();
            System.out.println(cells);
            
            Game game = new Game(gameServer, user);
            GameInterface gameStub = (GameInterface) UnicastRemoteObject.exportObject(game,0);
            registry.rebind("GameClient" + user, gameStub);
            game.Play();
            
//            for (int i = 0; i < 5; i++) {
//            	System.out.println("Welche Zelle?");
//            	String cell = scan.nextLine();
//            	game.setCell(cell, user);
//            	cells = game.getCells();
//            	System.out.println(cells);				
//			}
            
            server.logout(user);
            scan.close();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	}

}
