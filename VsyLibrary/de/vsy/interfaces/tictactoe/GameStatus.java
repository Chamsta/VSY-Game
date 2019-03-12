package de.vsy.interfaces.tictactoe;


/**
 * @author Gladis
 *
 */
public enum GameStatus {
	/**
	 * The game status is unknown or the game is not initialize.
	 */
	None(1),
	/**
	 * The game is running.
	 */
	Running(2),
	/**
	 * The game is end.
	 */
	Terminated(3),
	/**
	 * The game is waiting for the second player to begin.
	 */
	Waiting(4);
	
	private final int nummer;
	
	private GameStatus(int nummer){
		this.nummer = nummer;
	}
	
	public int getNummer(){
		return this.nummer;
	}
	
	public static GameStatus getEnum(int nummer) {
		switch(nummer){
			case 1: return GameStatus.None;
			case 2: return GameStatus.Running;
			case 3: return GameStatus.Terminated;
			case 4: return GameStatus.Waiting;
			default: return GameStatus.None;
		}
	}
}
