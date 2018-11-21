package objects;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import dbconnect.DBConnection;
import de.vsy.interfaces.GameInterface;
import de.vsy.interfaces.ServerInterface;
import de.vsy.interfaces.tictactoe.GameStatus;

public class Server implements ServerInterface {
	public static DBConnection dbConnection;
	private static Registry registry;
	
	public Server(Registry registry) {
		Server.registry = registry;
		dbConnection = new DBConnection();
	}

	@Override
	public void login(String user) throws RemoteException {
		try {
			dbConnection.loginUser(user);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public int getGameId(String user) throws RemoteException {
		try {
			Integer gameId = dbConnection.getGameIdForUser(user);
			if(gameId == null) {
				gameId = findWaitingGame(user);
				if(gameId == null)
					gameId = createGame(user);
			} else {
				registerGame(gameId);
			}
			return gameId;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void logout(String user) throws RemoteException {
		try {
			dbConnection.logoutUser(user);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	private int createGame(String user) throws Exception{
		Game game = new Game(-1);
		game.setPlayer1(user);
		game.setStatus(GameStatus.Waiting);
		dbConnection.insertNewGame(game);
		try {
			String reg ="Game" + game.getId();
			GameInterface gameStub = (GameInterface) UnicastRemoteObject.exportObject(game,0);
			registry.bind(reg, gameStub);
			System.out.println("Registered " + reg);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return game.getId();
	}
	
	private void registerGame(int gameId) throws Exception {
		Game game = dbConnection.getGame(gameId);
		String reg ="Game" + game.getId();
		GameInterface gameStub = (GameInterface) UnicastRemoteObject.exportObject(game,0);
		registry.rebind(reg, gameStub);
		System.out.println("Registered " + reg);
	}
	
	private Integer findWaitingGame(String user) throws Exception {
		Game game = dbConnection.findWaitingGame(user);
		if(game != null) {
			registerGame(game.getId());
			return game.getId();
		}
		return null;
	}
}
