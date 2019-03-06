package object;

import java.rmi.RemoteException;
import java.util.HashMap;

import de.vsy.interfaces.IGame;
import de.vsy.interfaces.tictactoe.GameStatus;
import gui.GameBoard;

public class GameClient implements IGame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7513278496760323195L;
	private IGame gameServer;
	private GameBoard gameBoard;
	private String player;
	int gameSize;
	int id;
	
	public GameClient(IGame gameServer, String player) throws RemoteException{
		this.gameServer = gameServer;
		this.gameSize = gameServer.getGameSize();
		this.player = player;
		this.id = gameServer.getId();
		this.gameBoard = new GameBoard(gameSize, this);
		this.gameBoard.setPlayer(player);
	}
	
	public boolean setCell(String key) throws RemoteException {
		return this.setCell(key, this.player);
	}

	@Override
	public HashMap<String, Boolean> getCells() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getCell(int positionX, int positionY) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getCell(String key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setCell(int positionX, int positionY, Boolean value) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Diese Methode wird vom Server aufgerufen und setzt die entsprechende Zelle auf dem GUI
	 */
	@Override
	public Boolean setCell(String key, Boolean value) throws RemoteException {
		this.gameBoard.setValue(key, value);
		return true;
	}

	/**
	 * Diese Methode wird aus dem GUI aufgerufen, und schickt dem Server welche Zelle von dem Spieler dieses Clients angeklickt wurde.
	 * Der Server bearbeitet diese Anfrage und ruft das die obere setCell Methode auf beiden Clients auf um das Feld zu setzen.
	 */
	@Override
	public Boolean setCell(String key, String player) throws RemoteException {
		if(this.getStatus() == GameStatus.Terminated) return false;
		return this.gameServer.setCell(key, player);
	}

	@Override
	public int getGameSize() throws RemoteException {
		return this.gameServer.getGameSize();
	}

	@Override
	public String getNextPlayer() throws RemoteException {
		return this.gameServer.getNextPlayer();
	}

	/**
	 * Wird vom Server aufgerufen und gibt an das GUI weiter wer dran ist.
	 */
	@Override
	public void setNextPlayer(String nextPlayer) throws RemoteException {
		if(player.equals(nextPlayer)) {
			gameBoard.setMyTurn(true);
		}else {
			gameBoard.setMyTurn(false);			
		}
		gameBoard.setNextPlayer(nextPlayer);
	}

	@Override
	public String getPlayer1() throws RemoteException {
		String player1 = this.gameServer.getPlayer1();
		setPlayer1(player1);
		return player1;
	}

	@Override
	public void setPlayer1(String value) throws RemoteException {
		this.gameBoard.setPlayer1(value);
	}

	@Override
	public String getPlayer2() throws RemoteException {
		String player2 = this.gameServer.getPlayer2();
		setPlayer2(player2);
		return player2;
	}

	@Override
	public void setPlayer2(String value) throws RemoteException {
		this.gameBoard.setPlayer2(value);
	}

	@Override
	public GameStatus getStatus() throws RemoteException {
		return gameServer.getStatus();
	}

	@Override
	public void setStatus(GameStatus status) throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public int getId() {
		return id;
	}

	/**
	 * Startet das Spiel und zeigt das GUI an
	 */
	@Override
	public void Play() throws RemoteException {
		HashMap<String, Boolean> map = this.gameServer.getCells();
		for(String key : map.keySet()){
			gameBoard.setValue(key, map.get(key));
		}
		this.gameBoard.start();
		setNextPlayer(this.gameServer.getNextPlayer());
		getPlayer1();
		getPlayer2();
	}

	@Override
	public void Stop() throws RemoteException {
		gameBoard.stop();
	}

	@Override
	public void SwitchPlayer() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getWinner() throws RemoteException {
		return this.gameServer.getWinner();
	}

}
