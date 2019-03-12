package de.vsy.classes.tictactoe;

/**
 * This class contains information about registred server.
 */
public final class ServerInfo {
	/**
	 * The hostname.
	 */
	public final String Name;
	
	/**
	 * The port which the server is connected.
	 */
    public final int Port;
    
    /**
     * The constructor.
     * @param name The hostname.
     * @param port The port number.
     */
    public ServerInfo(String name, int port) {
        this.Name = name;
        this.Port = port;
    }
}