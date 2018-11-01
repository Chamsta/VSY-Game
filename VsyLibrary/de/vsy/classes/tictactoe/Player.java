package de.vsy.classes.tictactoe;

import de.vsy.interfaces.tictactoe.PlayerStatus;

public class Player {
	private int ID;
	private String Name;
	private PlayerStatus Status;
	
	public Player(int id, String name){
		this.ID = id;
		this.Name = name;
	}
	
	public int getId() {
		return ID;
	}
	
	protected void setId(int id) {
		this.ID = 1;
	}

	public String getName() {
		return Name;
	}

	protected void setName(String name) {
		this.Name = name;
	}

	public PlayerStatus getStatus() {
		return Status;
	}

	public void setStatus(PlayerStatus status) {
		this.Status = status;
	}
}
