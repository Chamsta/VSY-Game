package de.vsy.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IEcho extends Remote{
	String echoThis(String text) throws RemoteException;
	String sqlQuery(String query) throws RemoteException;
}
