package object;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import de.vsy.interfaces.IGame;
import de.vsy.interfaces.IServer;

public class ServerFinder {
	private static List<String> listServers;
	private static String host;
	private static final int RMI_PORT_MIN = 2000;
	private static final int RMI_PORT_MAX = 49150;
	private static IServer server;
	private static Registry registry;
	private static IGame gameServer;
	
	public static IServer getServer(String initHost) throws Exception {
		if(initHost != null) {
			host = initHost;
		}
		if(listServers == null) {
			listServers = new ArrayList<String>();
			initServerList(host);
		}
		try {
			if(server.ping()) {
				return server;
			}
		} catch (ConnectException e) {
			for(String address : listServers) {
				String host = address.split(":")[0];
				int port = Integer.valueOf(address.split(":")[1]);
				registry = LocateRegistry.getRegistry(host, port);
				try {
					if(registry.list().length != 0) {
						server = (IServer) registry.lookup("Server");
						try {
							if(server.ping()) {
								server.checkServers();
								listServers = server.getServerList();
								gameServer = null;
								return server;
							}
						} catch (Exception e2) {
							continue;
						}
					}
				} catch(ConnectException e1) {
					continue;
				}
			}
		}
		throw new Exception("Keine Server verfügbar...");
	}

	public static Registry getRegistry(String initHost) throws Exception {
		if(initHost != null) {
			host = initHost;
		}
		if(listServers == null) {
			listServers = new ArrayList<String>();
			initServerList(host);
		}
		try {
			if(server.ping()) {
				return registry;
			}
		} catch (ConnectException e) {
			for(String address : listServers) {
				String host = address.split(":")[0];
				int port = Integer.valueOf(address.split(":")[1]);
				registry = LocateRegistry.getRegistry(host, port);
				try {
					if(registry.list().length != 0) {
						server = (IServer) registry.lookup("Server");
						try {
							if(server.ping())
								return registry;
						} catch (Exception e2) {
							continue;
						}
					}
				} catch (ConnectException e1) {
					continue;
				}
			}
		}
		throw new Exception("Keine Server verfügbar...");
	}
	
	public static IGame getGameServer(String user) throws Exception {
		try {
			if(server.ping() && gameServer != null) {
				return gameServer;
			}
		} catch (Exception e) {
			getServer(null);
		}
		 // Gets the GameID for this player
        int id = server.getGameId(user);
        System.out.println("Game id is: " + id);
        // Gets the remote object game for this player
        String reg = "Game"+id;
        System.out.println("Getting " + reg);
        gameServer = (IGame) registry.lookup(reg);
        return gameServer;
	}
	
	private static void initServerList(String host) throws Exception {
		registry = findRegistry(host);
		server = (IServer) registry.lookup("Server");
		server.checkServers();
		listServers = server.getServerList();
	}

	private static Registry findRegistry(String host) throws Exception {
		Registry registry;
		for(int port = RMI_PORT_MIN; port <= RMI_PORT_MAX; port++) {
			try {
				registry = LocateRegistry.getRegistry(host, port);
				if(registry.list().length != 0) {
					return registry;
				}
			} catch (RemoteException e) {
				continue;
			}
		}
		throw new Exception("Registry not found");
	}
	
	public static void reloadServerList() throws Exception {
		try {
			listServers = server.getServerList();
		} catch (ConnectException e) {
			server = getServer(null);
			listServers = server.getServerList();
		}
	}
}
