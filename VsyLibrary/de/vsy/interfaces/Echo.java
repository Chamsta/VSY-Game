package de.vsy.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Echo extends Remote{
	String echoThis(String text) throws RemoteException;
}
