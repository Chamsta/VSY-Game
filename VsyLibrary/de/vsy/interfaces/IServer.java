package de.vsy.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * The interface to communicate with remote server.
 */
public interface IServer extends Remote{
	/**
	 * Logs in the player into the server.
	 * @param user The player name to log in.
	 */
	void login(String user) throws RemoteException;
	
	/**
	 * Gets the game identification for an user.
	 * @param user The player name
	 */
	int getGameId(String user) throws RemoteException;
		
	/**
	 * Logs out the player from the server.
	 * @param user The player name to log out.
	 */
	void logout(String user) throws RemoteException;
		
	/**
	 * Adds the client game for updating the game board
	 * @param gameId The game identification.
	 * @param user The player for this client game.
	 * @param clientGame The client game.
	 */
	void addClientGame(int gameId, String user, IGame clientGame) throws RemoteException;
		
	/**
	 * Gets the list of servers.
	 * @return The servers list.
	 */
	List<String> getServerList() throws RemoteException;
		
	/**
	 * Ping the server if it is available.
	 * @return true if it is available, otherwise false.
	 */
	boolean ping() throws RemoteException;
		
	/**
	 * Checks all servers if they are available.
	 */
	void checkServers() throws RemoteException;
		
	/**
	 * Notify all clients.
	 */
	void informClients() throws RemoteException;
}