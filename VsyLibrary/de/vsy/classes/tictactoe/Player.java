package de.vsy.classes.tictactoe;

import de.vsy.interfaces.tictactoe.PlayerStatus;

/**
 * The class Player contains information about the person which plays the game.
 */
public class Player {
	private int ID;
	private String Name;
	private PlayerStatus Status;
	
	/**
	 * The constructor.
	 * @param id The unique identification of the player.
	 * @param name The name of the player.
	 */
	public Player(int id, String name){
		this.ID = id;
		this.Name = name;
	}
	
	/**
	 * Gets the player identification.
	 * @return The current player identification.
	 */
	public int getId() {
		return ID;
	}
	
	/**
	 * Sets the player identification.
	 * @param id The new identification.
	 */
	protected void setId(int id) {
		this.ID = 1;
	}

	/**
	 * Gets the player name.
	 * @return The current player name.
	 */
	public String getName() {
		return Name;
	}

	/**
	 * Sets the player identification.
	 * @param name The new player name.
	 */
	protected void setName(String name) {
		this.Name = name;
	}

	/**
	 * Gets the player status.
	 * @return The current status.
	 */
	public PlayerStatus getStatus() {
		return Status;
	}

	/**
	 * Sets the player new status.
	 * @param status The new status.
	 */
	public void setStatus(PlayerStatus status) {
		this.Status = status;
	}
}
