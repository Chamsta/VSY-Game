package objects;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import dbconnect.DBConnection;
import de.vsy.interfaces.GameInterface;
import de.vsy.interfaces.ServerInterface;
import de.vsy.interfaces.tictactoe.GameStatus;

public class Server implements ServerInterface {
	public static DBConnection dbConnection;
	private static Registry registry;
	private static HashMap<Integer, GameServer> mapGames = new HashMap<Integer, GameServer>();
	
	public Server(Registry registry) {
		Server.registry = registry;
		dbConnection = new DBConnection();
	}

	/**
	 * Der user wird eingeloggt.
	 */
	@Override
	public void login(String user) throws RemoteException {
		try {
			if(dbConnection.isUserLoggedIn(user)){
				throw new RemoteException("User " + user + " ist bereits eingeloggt.");
			}
			dbConnection.loginUser(user);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * Für den gegebenen User wird ein Spiel gesucht. Erst wird nach einem Spiel gesucht, welches den spieler schon eingetragen hat.
	 * Danach wird nach einem Spiel gesucht, dass auf einen zweiten Spieler wartet.
	 * Falls beides nicht gefunden wird, wird ein neues Spiel angelegt.
	 */
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

	/**
	 * Der User wird ausgeloggt.
	 */
	@Override
	public void logout(String user) throws RemoteException {
		try {
			dbConnection.logoutUser(user);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * Ein neues Spiel für den User wird erstellt und in der Datenbank angelegt.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private int createGame(String user) throws Exception{
		GameServer game = new GameServer(-1);
		game.setPlayer1(user);
		game.setStatus(GameStatus.Waiting);
		dbConnection.insertNewGame(game, user);
		registerGame(game.getId());
		return game.getId();
	}
	
	/**
	 * Das Game wird in der Registry publiziert unter dem Namen "Game<id>".
	 * Die Games werden auf dem Server in einer Hashmap gehalten um die Referenzen zu den Clients nicht zu verlieren, da diese nicht in die Datenbank geschrieben werden können.
	 * @param gameId
	 * @throws Exception
	 */
	private void registerGame(int gameId) throws Exception {
		GameServer game = mapGames.get(gameId);
		if(game == null) {
			game = dbConnection.getGame(gameId);
			String reg ="Game" + game.getId();
			GameInterface gameStub = (GameInterface) UnicastRemoteObject.exportObject(game, Registry.REGISTRY_PORT);
			registry.rebind(reg, gameStub);
			mapGames.put(game.getId(), game);
			System.out.println("Registered " + reg);
		}
		game.Play();
	}
	
	/**
	 * Sucht ein wartendes Spiel, also ein spiel indem noch kein zweiter Spieler ist.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private Integer findWaitingGame(String user) throws Exception {
		GameServer game = dbConnection.findWaitingGame(user);
		if(game != null) {
			game.setPlayer2(user);
			registerGame(game.getId());
			return game.getId();
		}
		return null;
	}
	
	/**
	 * Wenn das Spiel vorbei ist wird es aus der Registry entfernt.
	 * @param gameId
	 */
	public static void removeGame(int gameId) {
		try {
			registry.unbind("Game" + gameId);
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
		}
	}

	/**
	 * Fügt dem Spiel das Remote Object zu dem Client game hinzu, über diese Remote Objecte kann der Server informationen an die beiden Clients schicken.
	 */
	@Override
	public void addClientGame(int gameId, String user, GameInterface clientGame) throws RemoteException {
		System.out.println("Adding Client game " + user + gameId);
		GameServer game = mapGames.get(gameId);
		if(game == null) {
			throw new RemoteException("Game mit dieser ID nicht gefunden.");
		}
		if(game.getPlayer1().equals(user)) {
			game.setClientGame1(clientGame);
		} else if(game.getPlayer2() == null || game.getPlayer2().equals(user)) {
			game.setClientGame2(clientGame);
		} else {
			throw new RemoteException("Dieser Spieler ist nicht in dem Game vorhanden.");
		}
	}
}
