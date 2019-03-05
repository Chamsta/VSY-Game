package de.vsy.interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import de.vsy.interfaces.tictactoe.GameStatus;

public interface IGame extends Remote, Serializable{

	/**
	 * @return the cells
	 */
	HashMap<String, Boolean> getCells() throws RemoteException;

	Boolean getCell(int positionX, int positionY) throws RemoteException;

	Boolean getCell(String key) throws RemoteException;

	/**
	 * @param cells the cells to set
	 */

	Boolean setCell(int positionX, int positionY, Boolean value) throws RemoteException;

	Boolean setCell(String key, Boolean value) throws RemoteException;
	
	Boolean setCell(String key, String player) throws RemoteException;

	/**
	 * @return the gameSize
	 */
	int getGameSize() throws RemoteException;

	/**
	 * @return the nextPlayer
	 */
	String getNextPlayer() throws RemoteException;

	/**
	 * @return the nextPlayer
	 */
	String getWinner() throws RemoteException;

	/**
	 * @param nextPlayer the nextPlayer to set
	 */
	void setNextPlayer(String nextPlayer) throws RemoteException;

	/**
	 * @return the player1
	 */
	String getPlayer1() throws RemoteException;

	/**
	 * @param player1 the player1 to set
	 */
	void setPlayer1(String value) throws RemoteException;

	/**
	 * @return the player2
	 */
	String getPlayer2() throws RemoteException;

	/**
	 * @param player2 the player2 to set
	 */
	void setPlayer2(String value) throws RemoteException;

	/**
	 * @return the status
	 */
	GameStatus getStatus() throws RemoteException;

	/**
	 * @param status the status to set
	 */
	void setStatus(GameStatus status) throws RemoteException;

	void Play() throws RemoteException;

	void Stop() throws RemoteException;

	void SwitchPlayer() throws RemoteException;
	
	/**
	 * get the Id
	 */
	int getId() throws RemoteException;
}