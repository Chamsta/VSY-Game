package gui;

import javax.swing.JOptionPane;

public class Login {
	private static String username;
	private static String server;
	public static String getUsername() {
		if(username != null)
			return username;
		while (username == null || username.isEmpty()){
			username = JOptionPane.showInputDialog("Benutzernamen eingeben");
		}
		return username;
	}
	
	public static String getServer() {
		if(server != null)
			return server;
		server = JOptionPane.showInputDialog("Serveradresse eingeben", "vom-Wege.de");
		while (server == null || server.isEmpty()){
			server = JOptionPane.showInputDialog("Serveradresse eingeben", "vom-Wege.de");
		}
		return server;
	}
}
