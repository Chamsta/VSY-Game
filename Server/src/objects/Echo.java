package objects;

import java.rmi.RemoteException;

import dbconnect.DBConnection;
import de.vsy.interfaces.IEcho;

public class Echo implements IEcho {
	@Override
	public String echoThis(String text) throws RemoteException {
		return "Echo " + text;
	}

	@Override
	public String sqlQuery(String query) throws RemoteException {
		DBConnection dbConnection = new DBConnection();
		String test = dbConnection.execute(query);
		return test;
	}
}