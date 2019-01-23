package de.vsy.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.vsy.classes.tictactoe.ServerInfo;

public interface IEcho extends Remote{
	String echoThis(String text) throws RemoteException;
	String sqlQuery(String query) throws RemoteException;
	List<ServerInfo> getServers() throws RemoteException;
}
