package main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

import intface.Echo;


public class Main {

	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "Ich bin dein Client.");
		String host = (args.length < 1) ? null : args[0];
		System.out.println(host);
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Echo stub = (Echo) registry.lookup("Echo");
            String response = stub.echoThis("Hallo Du da.");
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
	}

}
