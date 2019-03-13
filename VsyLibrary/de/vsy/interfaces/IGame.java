package de.vsy.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import de.vsy.interfaces.tictactoe.GameStatus;

/**
 * The interface to communicate with remote game.
 */
public interface IGame extends Remote, Serializable{

	/**
	 * Gets the list of cells.
	 * @return the cells
	 */
	HashMap<String, Boolean> getCells() throws RemoteException;

	/**
	 * Gets the cell value on specified position.
	 * @param positionX The column position.
	 * @param positionY The row position.
	 * @return the cell value.
	 */
	Boolean getCell(int positionX, int positionY) throws RemoteException;

	/**
	 * Gets the cell value of specified key position.
	 * @param key The key coordinate.
	 * @return the cell value.
	 */
	Boolean getCell(String key) throws RemoteException;

	/**
	 * Sets the cell value on specified position.
	 * @param positionX The column position.
	 * @param positionY The row position.
	 * @param value the cell value to set.
	 * @return the cell value saved.
	 */
	Boolean setCell(int positionX, int positionY, Boolean value) throws RemoteException;

	/**
	 * Sets the cell value of specified key position.
	 * @param key The key position.
	 * @param value the cell value to set.
	 * @return the cell value saved.
	 */
	Boolean setCell(String key, Boolean value) throws RemoteException;
	
	/**
	 * Sets the cell for the specified player.
	 * @param key The key coordinate.
	 * @param player The player name.
	 * @return the cell value saved.
	 */
	Boolean setCell(String key, String player) throws RemoteException;

	/**
	 * Gets the size of game.
	 * @return the game size.
	 */
	int getGameSize() throws RemoteException;

	/**
	 * Gets the player that playing next.
	 * @return the player is playing next.
	 */
	String getNextPlayer() throws RemoteException;

	/**
	 * Gets the winner of this game.
	 * @return the winner name of this game.
	 */
	String getWinner() throws RemoteException;

	/**
	 * Sets the next player.
	 * @param nextPlayer the player name to set.
	 */
	void setNextPlayer(String nextPlayer) throws RemoteException;

	/**
	 * Gets the first player.
	 * @return the first player.
	 */
	String getPlayer1() throws RemoteException;

	/**
	 * Sets the first player to the game.
	 * @param value the player name to set
	 */
	void setPlayer1(String value) throws RemoteException;

	/**
	 * Gets the second player.
	 * @return the second player.
	 */
	String getPlayer2() throws RemoteException;

	/**
	 * Sets the second player to the game.
	 * @param value the player name to set
	 */
	void setPlayer2(String value) throws RemoteException;

	/**
	 * Gets the game status.
	 * @return the status
	 */
	GameStatus getStatus() throws RemoteException;

	/**
	 * Sets the game status.
	 * @param status the status to set
	 */
	void setStatus(GameStatus status) throws RemoteException;

	/**
	 * Starts the game.
	 */
	void Play() throws RemoteException;

	/**
	 * Stops the game.
	 */
	void Stop() throws RemoteException;

	/**
	 * Switches the player that playing next.
	 */
	void SwitchPlayer() throws RemoteException;
	
	/**
	 * get the Id
	 */
	int getId() throws RemoteException;
	
	
	/**
	 * Server Management
	 */
	void reloadServerList() throws RemoteException;
}