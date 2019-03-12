package de.vsy.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface to communicate with remote object of Tic-toc-toe.
 */
public interface IEcho extends Remote{
	/**
	 * Sends the message to the remote object of Tic-toc-toe.
	 * @param text The message text to send.
	 */
	String echoThis(String text) throws RemoteException;
	
	/**
	 * Execute the sql query on the remote object.
	 * @param query The sql query to execute.
	 * @return The response that contains the results of the query.
	 */
	String sqlQuery(String query) throws RemoteException;
	
	/**
	 * Gets the servers list from the remote object.
	 * @return The list of servers.
	 */
	Object[] getServers() throws RemoteException;
}
