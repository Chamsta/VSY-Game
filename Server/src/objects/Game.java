package objects;

import java.rmi.RemoteException;
import java.util.HashMap;

import de.vsy.interfaces.GameInterface;
import de.vsy.interfaces.tictactoe.GameStatus;

/**
 * @author Gladis
 *
 */
public class Game implements GameInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -293274928239420221L;
	static int DEFAULT_GAME_SIZE = 3;
	private int id;
	private int gameSize;
	private String nextPlayer;
	private String player1;
	private String player2;	
	private GameStatus status;
	private GameInterface gameClient1;
	private GameInterface gameClient2;
	
	/**
	 * 
	 */
	public Game(int id){
		this(id, DEFAULT_GAME_SIZE);
	}

	/**
	 * @param size
	 */
	public Game(int id, int size){
		this.id = id;
		this.gameSize = size < DEFAULT_GAME_SIZE ? DEFAULT_GAME_SIZE : size;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getCells()
	 */
	@Override
	public HashMap<String, Boolean> getCells() throws RemoteException {
		return loadCells();
	}
	
	private HashMap<String, Boolean> loadCells() throws RemoteException {
		try {
			return Server.dbConnection.getCells(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getCell(int, int)
	 */
	@Override
	public Boolean getCell(int positionX, int positionY) throws RemoteException {
		HashMap<String, Boolean> cells = loadCells();
		if(positionX < 0 || positionX >= this.gameSize || positionY < 0 || positionY >= this.gameSize) 
			return null;
		return cells.getOrDefault(String.format("%d,%d", positionX, positionY), null);
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getCell(java.lang.String)
	 */
	@Override
	public Boolean getCell(String key) throws RemoteException {
		String[] coordinate = key.split(",");
		int posX = 0;
		int posY = 0;
		try{
			if(coordinate.length == 2){
				posX = Integer.valueOf(coordinate[0]);
				posY = Integer.valueOf(coordinate[1]);			
			}
		}
		catch(Exception ex){
			posX = -1;
			posY = -1;
		}		
		return this.getCell(posX, posY);
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setCell(int, int)
	 */
	@Override
	public Boolean setCell(int positionX, int positionY, Boolean value) throws RemoteException {
		if(positionX < 0 || positionX > this.gameSize || positionY < 0 || positionY > this.gameSize) 
			return null;
		String key = String.format("%d,%d", positionX, positionY);
		try {
			Server.dbConnection.setCell(id, key, value);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
		checkGameEnd();
		return value;
	}
	
	private void checkGameEnd() throws RemoteException {
		boolean end = true;
		for(Boolean val : getCells().values()){
			if(val == null) {
				end = false;
				break;
			}
		}
		if(end) {
			Stop();
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setCell(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public Boolean setCell(String key, Boolean value) throws RemoteException {
		String[] coordinate = key.split(",");
		int posX = 0;
		int posY = 0;
		try{
			if(coordinate.length == 2){
				posX = Integer.valueOf(coordinate[0]);
				posY = Integer.valueOf(coordinate[1]);			
			}
		}
		catch(Exception ex){
			posX = -1;
			posY = -1;
		}
		if(getGameClient1() != null) {
			gameClient1.setCell(key, value);
		}
		if(getGameClient2() != null) {
			gameClient2.setCell(key, value);
		}
		SwitchPlayer();
		return this.setCell(posX, posY, value);
	}
	
	public Boolean setCell(String key, String player) throws RemoteException {
		Boolean value = null;
		if(player.equals(player1))
			value = true;
		if(player.equals(player2))
			value = false;
		return setCell(key, value);
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getGameSize()
	 */
	@Override
	public int getGameSize() throws RemoteException {
		return gameSize;
	}

	/**
	 * @param gameSize the gameSize to set
	 */
	protected void setGameSize(int gameSize) {
		this.gameSize = gameSize < DEFAULT_GAME_SIZE ? DEFAULT_GAME_SIZE : gameSize;
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getNextPlayer()
	 */
	@Override
	public String getNextPlayer() throws RemoteException {
		try {
			this.nextPlayer = Server.dbConnection.getNextPlayer(id);
			if(this.nextPlayer == null && this.player2 != null)
				setNextPlayer(player1);
			return this.nextPlayer;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setNextPlayer(java.lang.String)
	 */
	@Override
	public void setNextPlayer(String nextPlayer) throws RemoteException {
		this.nextPlayer = nextPlayer;
		try {
			Server.dbConnection.setNextPlayer(id, this.nextPlayer);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
		if(getGameClient1() != null){
			gameClient1.setNextPlayer(this.nextPlayer);
		}
		if(getGameClient2() != null) {
			gameClient2.setNextPlayer(this.nextPlayer);
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getPlayer1()
	 */
	@Override
	public String getPlayer1() throws RemoteException {
		try {
			return this.player1 = Server.dbConnection.getPlayer1(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setPlayer1(java.lang.String)
	 */
	@Override
	public void setPlayer1(String value) throws RemoteException {
		this.player1 = value;
		if(getGameClient1() != null)
			gameClient1.setPlayer1(value);
		if(getGameClient2() != null)
			gameClient2.setPlayer1(value);
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getPlayer2()
	 */
	@Override
	public String getPlayer2() throws RemoteException {
		try {
			return this.player2 = Server.dbConnection.getPlayer2(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setPlayer2(java.lang.String)
	 */
	@Override
	public void setPlayer2(String value) throws RemoteException {
		this.player2 = value;
		if(getGameClient1() != null)
			gameClient1.setPlayer2(value);
		if(getGameClient2() != null)
			gameClient2.setPlayer2(value);
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getStatus()
	 */
	@Override
	public GameStatus getStatus() throws RemoteException {
		return status;
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setStatus(de.vsy.interfaces.tictactoe.GameStatus)
	 */
	@Override
	public void setStatus(GameStatus status) throws RemoteException {
		this.status = status;
		try {
			Server.dbConnection.setStatus(id, status);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	public HashMap<String, Boolean> getInitCells(){
		HashMap<String, Boolean> cells = new HashMap<String, Boolean>();
		
		for (int i = 1; i <= this.gameSize; i++) {
			for (int j = 1; j <= this.gameSize; j++) {
				cells.put(String.format("%d,%d", i, j), null);
			}
		}
		return cells;
	}
	
	private GameInterface getGameClient1() throws RemoteException {
		if(gameClient1 != null)
			return gameClient1;
		gameClient1 = Server.getClientGame(getPlayer1());
		return gameClient1;
	}
	
	private GameInterface getGameClient2() throws RemoteException {
		if(gameClient2 != null)
			return gameClient2;
		gameClient2 = Server.getClientGame(getPlayer2());
		return gameClient2;
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#Play()
	 */
	@Override
	public void Play() throws RemoteException{
		if(this.player1 == null || this.player1.isEmpty() || this.player2 == null || this.player2.isEmpty())
			return;
		setNextPlayer(player1);
		setStatus(GameStatus.Running);
		checkGameEnd();
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#Stop()
	 */
	@Override
	public void Stop() throws RemoteException{
		setStatus(GameStatus.Terminated);
		setNextPlayer(null);
		if(getGameClient1() != null) {
			gameClient1.Stop();
		}
		if(getGameClient2() != null) {
			gameClient2.Stop();
		}
		Server.removeClientGame(player1);
		Server.removeClientGame(player2);
		Server.removeGame(id);
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#SwitchPlayer()
	 */
	@Override
	public void SwitchPlayer() throws RemoteException{
		this.nextPlayer = getNextPlayer();
		this.nextPlayer = this.nextPlayer.equals(this.player1) ? this.player2 : this.player1;
		setNextPlayer(nextPlayer);
	}
	
}
