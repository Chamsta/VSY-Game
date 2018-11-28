package objects;

import java.rmi.RemoteException;
import java.util.HashMap;

import de.vsy.interfaces.GameInterface;
import de.vsy.interfaces.tictactoe.GameStatus;

/**
 * @author Gladis
 *
 */
public class Game implements GameInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = -293274928239420221L;
	static int DEFAULT_GAME_SIZE = 3;
	private int id;
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
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	// Loop through all cells of the board and if one is found to be null then return false.
    // Otherwise the board is full.
	public boolean isBoardFull() throws RemoteException {
        boolean isFull = true; 
        HashMap<String, Boolean> board = loadCells();
        int bLength = board.size();
        for (int i = 0; i < bLength; i++) {
            for (int j = 0; j < bLength; j++) {
                if (board.get(String.format("%d,%d", i, j)) == null) {
                    isFull = false;
                    break;
                }
            }
            if(!isFull){
            	break;
            }
        }         
        return isFull;
    }
	 
    // Check to see if all three values are the same (and not null) indicating a win.
    private boolean checkRowCol(Boolean c1, Boolean c2, Boolean c3) {
        return ((c1 != null) && (c1 == c2) && (c2 == c3));
    }
     
    // Loop through rows and see if any are winners.
    private boolean checkRowsForWin(int currentRow) throws RemoteException { 
    	HashMap<String, Boolean> board = loadCells();
        int bLength = board.size();
        for (int i = 0; i < bLength - 2; i++) {
            if (checkRowCol(board.get(String.format("%d,%d", currentRow, i)), 
            				board.get(String.format("%d,%d", currentRow, i + 1)), 
            				board.get(String.format("%d,%d", currentRow, i + 2)))) {
                return true;
            }
        }
        return false;
    }
     
    // Loop through columns and see if any are winners.
    private boolean checkColumnsForWin(int currentCol) throws RemoteException {
    	HashMap<String, Boolean> board = loadCells();
        int bLength = board.size();
        for (int i = 0; i < bLength - 2; i++) {
            if (checkRowCol(board.get(String.format("%d,%d", i, currentCol)), 
            				board.get(String.format("%d,%d", i + 1, currentCol)), 
            				board.get(String.format("%d,%d", i + 2, currentCol)))) {
                return true;
            }
        }
        return false;
    }
 
    // Check the two diagonals to see if either is a win. Return true if either wins.
    private boolean checkDiagonalsForWin(int currentRow, int currentCol) throws RemoteException {
    	HashMap<String, Boolean> board = loadCells();
        int bLength = board.size();
    	boolean checked = false;
    	int beginOffsetRow = 0;
    	int beginOffsetCol = 0;
    	int endOffsetRow = 0;
    	int endOffsetCol = 0;
    	// Diagonal back slash
    	beginOffsetRow = currentRow - 2;
    	beginOffsetCol = currentCol - 2;
    	endOffsetRow = currentRow + 2;
    	endOffsetCol = currentCol + 2;
    	for(int offset = 2; offset > 0; offset--)
	    	if((currentRow - offset < 0) || (currentCol - offset < 0)){
	    		beginOffsetRow++;
	    		beginOffsetCol++;
    		}
    	
    	for(int offset = 2; offset > 0; offset--)
	    	if((currentRow + offset >= bLength) || (currentCol + offset >= bLength)){
	    		endOffsetRow--;
	    		endOffsetCol--;
	    	}
    	
    	if(endOffsetRow - beginOffsetRow > 1){
	    	for (int i = 0; i < endOffsetCol - beginOffsetRow; i++) {
	            if ((beginOffsetRow + i + 2) < bLength &&
	            		(beginOffsetCol + i + 2) < bLength &&
            			checkRowCol(board.get(String.format("%d,%d", beginOffsetRow + i, beginOffsetCol + i)), 
	            				board.get(String.format("%d,%d", beginOffsetRow + i + 1, beginOffsetCol + i + 1)), 
	            				board.get(String.format("%d,%d", beginOffsetRow + i + 2, beginOffsetCol + i + 2)))) {
	            	checked = true;
	            }
	        }
    	}
    	
    	// Diagonal slash
    	beginOffsetRow = currentRow + 2;
    	beginOffsetCol = currentCol - 2;
    	endOffsetRow = currentRow - 2;
    	endOffsetCol = currentCol + 2;
    	for(int offset = 2; offset > 0; offset--)
	    	if((currentCol - offset < 0) || (currentRow + offset >= bLength)){
	    		beginOffsetRow--;
	    		beginOffsetCol++;
    		}
    	
    	for(int offset = 2; offset > 0; offset--)
	    	if((currentCol + offset >= bLength) || (currentRow - offset < 0)){
	    		endOffsetRow++;
	    		endOffsetCol--;
	    	}
    	
    	if(beginOffsetRow - endOffsetRow > 1){
	    	for (int i = 0; i < endOffsetCol - beginOffsetCol -1; i++) {
	            if ((beginOffsetRow - i - 2) >= endOffsetRow &&
	            		(beginOffsetCol + i + 2) <= endOffsetCol &&
	            		checkRowCol(board.get(String.format("%d,%d", beginOffsetRow - i, beginOffsetCol + i)), 
	            				board.get(String.format("%d,%d", beginOffsetRow - i - 1, beginOffsetCol + i + 1)), 
	            				board.get(String.format("%d,%d", beginOffsetRow - i - 2, beginOffsetCol + i + 2)))) {
	            	checked = true;
	            }
	        }
    	}    	
    	
    	return checked;
    }
    
    // Returns true if there is a win, false otherwise.
    // This calls our other win check functions to check the entire board.
    public boolean checkForWin(int currentRow, int currentCol) throws RemoteException {
        return (checkRowsForWin(currentRow) || 
        		checkColumnsForWin(currentCol) || 
        		checkDiagonalsForWin(currentRow, currentCol));
    }
     
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getCells()
	 */
	@Override
	public HashMap<String, Boolean> getCells() throws RemoteException {
		return loadCells();
	}
	
	private HashMap<String, Boolean> loadCells() throws RemoteException {
		try {
			return Server.dbConnection.getCells(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getCell(int, int)
	 */
	@Override
	public Boolean getCell(int positionX, int positionY) throws RemoteException {
		HashMap<String, Boolean> cells = loadCells();
		if(positionX < 0 || positionX >= this.gameSize || positionY < 0 || positionY >= this.gameSize) 
			return null;
		return cells.getOrDefault(String.format("%d,%d", positionX, positionY), null);
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getCell(java.lang.String)
	 */
	@Override
	public Boolean getCell(String key) throws RemoteException {
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

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setCell(int, int)
	 */
	@Override
	public Boolean setCell(int positionX, int positionY, Boolean value) throws RemoteException {
		if(positionX < 0 || positionX > this.gameSize || positionY < 0 || positionY > this.gameSize) 
			return null;
		String key = String.format("%d,%d", positionX, positionY);
		try {
			Server.dbConnection.setCell(id, key, value);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setCell(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public Boolean setCell(String key, Boolean value) throws RemoteException {
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
		return this.setCell(posX, posY, value);
	}
	
	public Boolean setCell(String key, String player) throws RemoteException {
		Boolean value = null;
		if(player.equals(player1))
			value = true;
		if(player.equals(player2))
			value = false;
		return setCell(key, value);
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getGameSize()
	 */
	@Override
	public int getGameSize() throws RemoteException {
		return gameSize;
	}

	/**
	 * @param gameSize the gameSize to set
	 */
	protected void setGameSize(int gameSize) {
		this.gameSize = gameSize < DEFAULT_GAME_SIZE ? DEFAULT_GAME_SIZE : gameSize;
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getNextPlayer()
	 */
	@Override
	public String getNextPlayer() throws RemoteException {
		return this.nextPlayer;
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setNextPlayer(java.lang.String)
	 */
	@Override
	public void setNextPlayer(String nextPlayer) throws RemoteException {
		this.nextPlayer = nextPlayer;
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getPlayer1()
	 */
	@Override
	public String getPlayer1() throws RemoteException {
		return this.player1;
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setPlayer1(java.lang.String)
	 */
	@Override
	public void setPlayer1(String value) throws RemoteException {
		this.player1 = value;
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getPlayer2()
	 */
	@Override
	public String getPlayer2() throws RemoteException {
		return this.player2;
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setPlayer2(java.lang.String)
	 */
	@Override
	public void setPlayer2(String value) throws RemoteException {
		this.player2 = value;
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getStatus()
	 */
	@Override
	public GameStatus getStatus() throws RemoteException {
		return status;
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setStatus(de.vsy.interfaces.tictactoe.GameStatus)
	 */
	@Override
	public void setStatus(GameStatus status) throws RemoteException {
		this.status = status;
	}
	
	public HashMap<String, Boolean> getInitCells(){
		HashMap<String, Boolean> cells = new HashMap<String, Boolean>();
		
		for (int i = 1; i <= this.gameSize; i++) {
			for (int j = 1; j <= this.gameSize; j++) {
				cells.put(String.format("%d,%d", i, j), null);
			}
		}
		return cells;
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#Play()
	 */
	@Override
	public void Play() throws RemoteException{
		if(this.player1.isEmpty() || this.player2.isEmpty())
			return;
		while(this.status.equals(GameStatus.Running)){
			
		}
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#Stop()
	 */
	@Override
	public void Stop() throws RemoteException{
		this.status = GameStatus.Terminated;
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#SwitchPlayer()
	 */
	@Override
	public void SwitchPlayer() throws RemoteException{
		
		this.nextPlayer = this.nextPlayer.equals(this.player1) ? this.player2 : this.player1;
	}
	
}
