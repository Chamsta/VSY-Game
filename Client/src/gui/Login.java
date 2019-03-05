package gui;

import javax.swing.JOptionPane;

public class Login {
	private static String username;
	private static String server;
	public static String getUsername() {
		if(username != null)
			return username;
		
		do{
			username = JOptionPane.showInputDialog("Benutzernamen eingeben");
		}while (username == null || username.isEmpty());
		
		return username;
	}
	
	public static String getServer() {
		if(server != null)
			return server;
		
		do{
			server = JOptionPane.showInputDialog("Serveradresse eingeben", "localhost");
		}while (server == null || server.isEmpty());
		
		return server;
	}
}
