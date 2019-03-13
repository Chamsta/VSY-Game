package objects;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import dbconnect.DBConnection;
import de.vsy.classes.tictactoe.ServerInfo;
import de.vsy.interfaces.IEcho;

public class Echo implements IEcho {
	private List<ServerInfo> servers;
	
	/**
	 * The constructor.
	 */
	public Echo(){
		this.servers = new ArrayList<ServerInfo>();
	}
	
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

	@Override
	public Object[] getServers() throws RemoteException {
		return this.servers.toArray();
	}

	/**
	 * Adds server.
	 */
	public void addServer(ServerInfo server) throws RemoteException {
		this.servers.add(server);
	}
}