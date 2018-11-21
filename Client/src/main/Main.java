package main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Scanner;


import de.vsy.interfaces.Echo;
import de.vsy.interfaces.GameInterface;
import de.vsy.interfaces.ServerInterface;


public class Main { //Client

	public static void main(String[] args) {
		System.out.println("Ich bin dein Client.");
		String host = (args.length < 1) ? null : args[0];
		if(host == null){
			System.out.println("Bitte Adresse des Servers angeben!");
			System.exit(0);
		}
		System.out.println(host);
        try {
        	//Beim Starten aus Eclipse den vollständigen Pfad zu security.policy eingeben!
        	System.setProperty("java.security.policy", "security.policy");
        	System.setSecurityManager(new SecurityManager());
            Registry registry = LocateRegistry.getRegistry(host);
            Echo stub = (Echo) registry.lookup("Echo");
            String response = stub.echoThis("Hallo Du da.");
            System.out.println("response: " + response);
            
            ServerInterface server = (ServerInterface) registry.lookup("Server");
            
            System.out.println("Name eingeben");
            Scanner scan = new Scanner(System.in);
            String user = scan.nextLine();
            server.login(user);
            int id = server.getGameId(user);
            System.out.println("Game id is: " + id);
            
            String reg = "Game"+id;
            System.out.println("Getting " + reg);
            GameInterface game = (GameInterface) registry.lookup(reg);
            HashMap<String, Boolean> cells = game.getCells();
            System.out.println(cells);
            
            for (int i = 0; i < 5; i++) {
            	System.out.println("Welche Zelle?");
            	String cell = scan.nextLine();
            	game.setCell(cell, user);
            	cells = game.getCells();
            	System.out.println(cells);				
			}
            
            server.logout(user);
            scan.close();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	}

}
