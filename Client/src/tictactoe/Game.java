package tictactoe;

import java.util.HashMap;

/**
 * @author Gladis
 *
 */
public class Game {
	static int DEFAULT_GAME_SIZE = 3;
	private HashMap<String, Boolean> cells;
	private int gameSize;
	private String NextPlayer;
	private String Player1;
	private String Player2;	
	private GameStatus status;	
	
	/**
	 * 
	 */
	public Game(){
		this(3);
	}

	/**
	 * @param size
	 */
	public Game(int size){
		setGameSize(size < DEFAULT_GAME_SIZE ? DEFAULT_GAME_SIZE : size);
		setStatus(GameStatus.None);
		this.setNextPlayer("");
		this.Init();
	}

	/**
	 * @return the cells
	 */
	public HashMap<String, Boolean> getCells() {
		return cells;
	}

	/**
	 * @param cells the cells to set
	 */
	public void setCells(HashMap<String, Boolean> cells) {
		this.cells = cells;
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
	public void setGameSize(int gameSize) {
		this.gameSize = gameSize;
	}
	
	/**
	 * @return the nextPlayer
	 */
	public String getNextPlayer() {
		return NextPlayer;
	}

	/**
	 * @param nextPlayer the nextPlayer to set
	 */
	public void setNextPlayer(String nextPlayer) {
		NextPlayer = nextPlayer;
	}

	/**
	 * @return the player1
	 */
	public String getPlayer1() {
		return Player1;
	}

	/**
	 * @param player1 the player1 to set
	 */
	public void setPlayer1(String player1) {
		Player1 = player1;
	}

	/**
	 * @return the player2
	 */
	public String getPlayer2() {
		return Player2;
	}

	/**
	 * @param player2 the player2 to set
	 */
	public void setPlayer2(String player2) {
		Player2 = player2;
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
		
	}
	
	public void Stop(){
		
	}
	
	public boolean SwitchPlayer(){
		
		return false;
	}
	
}
