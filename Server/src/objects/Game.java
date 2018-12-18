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
	static int GAME_SIZE_MAX = 10;
	static int GAME_WIN_TIMES = 3;
	private int id;
	private int gameSize;
	private String nextPlayer;
	private String player1;
	private String player2;	
	private GameStatus status;
	private int winTimes;
	private GameInterface gameClient1;
	private GameInterface gameClient2;
	
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
		this.gameSize = size > GAME_SIZE_MAX ? GAME_SIZE_MAX : size;
		this.status = GameStatus.None;
		this.nextPlayer = "";
		this.winTimes = 0;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	@Override
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
        return ((checkRowsForWin(currentRow) || 
        			checkColumnsForWin(currentCol) || 
        			checkDiagonalsForWin(currentRow, currentCol)) &&
        		((this.winTimes == GAME_SIZE_MAX && this.gameSize > DEFAULT_GAME_SIZE)|| 
        				(this.gameSize == DEFAULT_GAME_SIZE)));
    }
     
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getCells()
	 */
	@Override
	public HashMap<String, Boolean> getCells() throws RemoteException {
		return loadCells();
	}
	
	/**
	 * Lädt die Cells zu dem Spiel aus der Datenbank.
	 * @return
	 * @throws RemoteException
	 */
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
	 * Und schreibt den geänderten Wert in die Datenbank
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
		checkGameEnd();
		return value;
	}
	
	/**
	 * Prüf, ob das Spiel zu Ende ist (Spielfeld voll)
	 * @throws RemoteException
	 */
	private void checkGameEnd() throws RemoteException {
		boolean end = true;
		for(Boolean val : getCells().values()){
			if(val == null) {
				end = false;
				break;
			}
		}
		if(end) {
			Stop();
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setCell(java.lang.String, java.lang.Boolean)
	 * Erweitert um die Funktion, dass die angemeldeten Clients über die Änderung der Zelle informiert werden.
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
		if(gameClient1 != null) {
			gameClient1.setCell(key, value);
		}
		if(gameClient2 != null) {
			gameClient2.setCell(key, value);
		}
		SwitchPlayer();
		return this.setCell(posX, posY, value);
	}
	
	/**
	 * Setzt eine Zelle, der Wert für die Zelle wird anhand des Spielernamens überprüft.
	 * Spieler 1 hat true. Spieler 2 hat false.
	 */
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
	 * Holt nextPlayer aus der Datenbank und setzt diesen über die setNextPlayer Methode.
	 */
	@Override
	public String getNextPlayer() throws RemoteException {
		try {
			this.nextPlayer = Server.dbConnection.getNextPlayer(id);
			if(this.nextPlayer == null && this.player2 != null)
				setNextPlayer(player1);
			return this.nextPlayer;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setNextPlayer(java.lang.String)
	 * Schreibt nextPlayer in die Datenbank und setzt diesen bei den Clients.
	 */
	@Override
	public void setNextPlayer(String nextPlayer) throws RemoteException {
		this.nextPlayer = nextPlayer;
		try {
			Server.dbConnection.setNextPlayer(id, this.nextPlayer);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
		if(gameClient1 != null){
			gameClient1.setNextPlayer(this.nextPlayer);
		}
		if(gameClient2 != null) {
			gameClient2.setNextPlayer(this.nextPlayer);
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getPlayer1()
	 * Ließt Player 1 aus der Datenbank
	 */
	@Override
	public String getPlayer1() throws RemoteException {
		try {
			return this.player1 = Server.dbConnection.getPlayer1(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setPlayer1(java.lang.String)
	 * Player 1 wird nicht in die Datenbank geschrieben, da dies beim Initialisieren des Games geschieht
	 * Bei den Clients wird Player1 gesetzt
	 */
	@Override
	public void setPlayer1(String value) throws RemoteException {
		this.player1 = value;
		if(gameClient1 != null)
			gameClient1.setPlayer1(value);
		if(gameClient2 != null)
			gameClient2.setPlayer1(value);
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#getPlayer2()
	 */
	@Override
	public String getPlayer2() throws RemoteException {
		try {
			return this.player2 = Server.dbConnection.getPlayer2(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#setPlayer2(java.lang.String)
	 */
	@Override
	public void setPlayer2(String value) throws RemoteException {
		this.player2 = value;
		if(gameClient1 != null)
			gameClient1.setPlayer2(value);
		if(gameClient2 != null)
			gameClient2.setPlayer2(value);
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
		try {
			Server.dbConnection.setStatus(id, status);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
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
	
	public void setClientGame1(GameInterface clientGame) {
		this.gameClient1 = clientGame;
	}
	
	public void setClientGame2(GameInterface clientGame) {
		this.gameClient2 = clientGame;
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#Play()
	 */
	@Override
	public void Play() throws RemoteException{
		if(this.player1 == null || this.player1.isEmpty() || this.player2 == null || this.player2.isEmpty())
			return;
		setNextPlayer(player1);
		setStatus(GameStatus.Running);
		checkGameEnd();
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#Stop()
	 */
	@Override
	public void Stop() throws RemoteException{
		setStatus(GameStatus.Terminated);
		setNextPlayer(null);
		if(gameClient1 != null) {
			gameClient1.Stop();
		}
		if(gameClient2 != null) {
			gameClient2.Stop();
		}
		Server.removeGame(id);
	}
	
	/* (non-Javadoc)
	 * @see de.vsy.classes.tictactoe.Game1#SwitchPlayer()
	 */
	@Override
	public void SwitchPlayer() throws RemoteException{
		this.nextPlayer = getNextPlayer();
		this.nextPlayer = this.nextPlayer.equals(this.player1) ? this.player2 : this.player1;
		setNextPlayer(nextPlayer);
	}
	
}
