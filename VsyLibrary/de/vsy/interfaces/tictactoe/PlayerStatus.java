package de.vsy.interfaces.tictactoe;

/**
 * The status of player.
 * @author Gladis
 */
public enum PlayerStatus {
	/**
	 * The player is not connected.
	 */
	Offline,
	/**
	 * The player is connected.
	 */
	Online,
	 /**
	 * The player is connected and waiting for the next player to play game.
	 */
	 Waiting
}
