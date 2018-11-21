package de.vsy.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
	void login(String user) throws RemoteException;
	int getGameId(String user) throws RemoteException;
	void logout(String user) throws RemoteException;
}