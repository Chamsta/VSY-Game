package de.vsy.classes.tictactoe;

import java.util.HashMap;

import de.vsy.interfaces.tictactoe.GameStatus;

/**
 * @author Gladis
 *
 */
public class Game {
	static int DEFAULT_GAME_SIZE = 3;
	private int id;
	private HashMap<String, Boolean> cells;
	private int gameSize;
	private String nextPlayer;
	private String player1;
	private String player2;	
	private GameStatus status;	
	
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
		this.status = GameStatus.None;
		this.nextPlayer = "";
		this.Init();
	}

	/**
	 * @return the cells
	 */
	public HashMap<String, Boolean> getCells() {
		return cells;
	}

	public Boolean getCell(int positionX, int positionY) {
		if(positionX < 0 || positionX >= this.gameSize || positionY < 0 || positionY >= this.gameSize) 
			return null;
		return cells.getOrDefault(String.format("%d,%d", positionX, positionY), null);
	}

	public Boolean getCell(String key) {
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

	/**
	 * @param cells the cells to set
	 */
	public void setCells(HashMap<String, Boolean> cells) {
		this.cells = cells;
	}

	public Boolean setCell(int positionX, int positionY) {
		if(positionX < 0 || positionX >= this.gameSize || positionY < 0 || positionY >= this.gameSize) 
			return null;
		return cells.put(String.format("%d,%d", positionX, positionY), null);
	}

	public Boolean setCell(String key, Boolean value) {
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
		return this.setCell(posX, posY);
	}

	/**
	 * @return the gameSize
	 */
	public int getGameSize() {
		return gameSize;
	}

	/**
	 * @param gameSize the gameSize to set
	 */
	protected void setGameSize(int gameSize) {
		this.gameSize = gameSize < DEFAULT_GAME_SIZE ? DEFAULT_GAME_SIZE : gameSize;
	}
	
	/**
	 * @return the nextPlayer
	 */
	public String getNextPlayer() {
		return this.nextPlayer;
	}

	/**
	 * @param nextPlayer the nextPlayer to set
	 */
	public void setNextPlayer(String nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	/**
	 * @return the player1
	 */
	public String getPlayer1() {
		return this.player1;
	}

	/**
	 * @param player1 the player1 to set
	 */
	public void setPlayer1(String value) {
		this.player1 = value;
	}

	/**
	 * @return the player2
	 */
	public String getPlayer2() {
		return this.player2;
	}

	/**
	 * @param player2 the player2 to set
	 */
	public void setPlayer2(String value) {
		this.player2 = value;
	}

	/**
	 * @return the status
	 */
	public GameStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(GameStatus status) {
		this.status = status;
	}
	
	private void Init(){
		if (this.cells == null) {
			cells = new HashMap<String, Boolean>();
		}
		
		if(!this.cells.isEmpty()){
			this.cells.clear();
		}
		
		for (int i = 1; i <= this.gameSize; i++) {
			for (int j = 1; j <= this.gameSize; j++) {
				this.cells.put(String.format("%d,%d", i, j), null);
			}
		}
	}
	
	public void Play(){
		if(this.player1.isEmpty() || this.player2.isEmpty())
			return;
		while(this.status.equals(GameStatus.Running)){
			
		}
	}
	
	public void Stop(){
		this.status = GameStatus.Terminated;
	}
	
	public void SwitchPlayer(){
		
		this.nextPlayer = this.nextPlayer.equals(this.player1) ? this.player2 : this.player1;
	}
	
}
