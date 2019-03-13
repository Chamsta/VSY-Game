package gui;

import javax.swing.JOptionPane;

/**
 * The Login gui dialog to get the game parameters.
 */
public class Login {
	private static String username;
	private static String server;
	
	/**
	 * Gets the input username to connect the client.
	 */
	public static String getUsername() {
		if(username != null)
			return username;
		
		do{
			username = JOptionPane.showInputDialog("Benutzernamen eingeben");
			if(username == null) {
				System.exit(0);
			}
		}while (username == null || username.isEmpty());
		
		return username;
	}
	
	/**
	 * Gets the input server to connect the client.
	 */
	public static String getServer() {
		if(server != null)
			return server;
		
		do{
			server = JOptionPane.showInputDialog("Serveradresse eingeben", "localhost");
			if(server == null) {
				System.exit(0);
			}
		}while (server == null || server.isEmpty());
		
		return server;
	}
}
