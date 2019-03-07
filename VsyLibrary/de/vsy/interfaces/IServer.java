package de.vsy.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServer extends Remote{
	void login(String user) throws RemoteException;
	int getGameId(String user) throws RemoteException;
	void logout(String user) throws RemoteException;
	void addClientGame(int gameId, String user, IGame clientGame) throws RemoteException;
	List<String> getServerList() throws RemoteException;
	boolean ping() throws RemoteException;
	void checkServers() throws RemoteException;
}