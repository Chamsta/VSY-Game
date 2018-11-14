package main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


import de.vsy.interfaces.Echo;


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
        	System.setSecurityManager(new SecurityManager());
            Registry registry = LocateRegistry.getRegistry(host);
            Echo stub = (Echo) registry.lookup("Echo");
            String response = stub.echoThis("Hallo Du da.");
            System.out.println("response: " + response);
            
            System.out.println("SQL Statement angeben:");
            String sql = new Scanner(System.in).nextLine();
            response = stub.sqlQuery(sql);
            System.out.println(response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	}

}
