package de.vsy.interfaces.tictactoe;


/**
 * @author Gladis
 *
 */
public enum GameStatus {
	None(1),
	Running(2),
	Terminated(3),
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
