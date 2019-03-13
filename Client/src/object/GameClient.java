package object;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.Timer;

import de.vsy.interfaces.IGame;
import de.vsy.interfaces.IServer;
import de.vsy.interfaces.tictactoe.GameStatus;
import gui.GameBoard;
import gui.ReconnectWindow;

/**
 * The client game.
 */
public class GameClient implements IGame {
	private static final long serialVersionUID = -7513278496760323195L;
	private IGame gameServer;
	private GameBoard gameBoard;
	private String player;
	int gameSize;
	int id;
	private Timer timer;
	
	/**
	 * The constructor.
	 * @param gameServer The remote game object
	 * @param player The player for this game.
	 */
	public GameClient(IGame gameServer, String player) throws RemoteException{
		this.gameServer = gameServer;
		this.gameSize = gameServer.getGameSize();
		this.player = player;
		this.id = gameServer.getId();
		this.gameBoard = new GameBoard(gameSize, this);
		this.gameBoard.setPlayer(player);
		initReloadTimer();
	}
	
	public boolean setCell(String key) throws RemoteException {
		return this.setCell(key, this.player);
	}

	@Override
	public HashMap<String, Boolean> getCells() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getCell(int positionX, int positionY) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getCell(String key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setCell(int positionX, int positionY, Boolean value) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Diese Methode wird vom Server aufgerufen und setzt die entsprechende Zelle auf dem GUI
	 */
	@Override
	public Boolean setCell(String key, Boolean value) throws RemoteException {
		this.gameBoard.setValue(key, value);
		return true;
	}

	/**
	 * Diese Methode wird aus dem GUI aufgerufen, und schickt dem Server welche Zelle von dem Spieler dieses Clients angeklickt wurde.
	 * Der Server bearbeitet diese Anfrage und ruft das die obere setCell Methode auf beiden Clients auf um das Feld zu setzen.
	 */
	@Override
	public Boolean setCell(String key, String player) throws RemoteException {
		try {
			if(this.getStatus() == GameStatus.Terminated) return false;
			return this.gameServer.setCell(key, player);
		} catch (ConnectException e) {
			reconnectWithWindow();
			if(this.getStatus() == GameStatus.Terminated) return false;
			return this.gameServer.setCell(key, player);
		}
	}

	@Override
	public int getGameSize() throws RemoteException {
		return this.gameServer.getGameSize();
	}

	@Override
	public String getNextPlayer() throws RemoteException {
		return this.gameServer.getNextPlayer();
	}

	/**
	 * Wird vom Server aufgerufen und gibt an das GUI weiter wer dran ist.
	 */
	@Override
	public void setNextPlayer(String nextPlayer) throws RemoteException {
		if(player.equals(nextPlayer)) {
			gameBoard.setMyTurn(true);
		}else {
			gameBoard.setMyTurn(false);			
		}
		gameBoard.setNextPlayer(nextPlayer);
	}

	@Override
	public String getPlayer1() throws RemoteException {
		String player1 = this.gameServer.getPlayer1();
		setPlayer1(player1);
		return player1;
	}

	@Override
	public void setPlayer1(String value) throws RemoteException {
		this.gameBoard.setPlayer1(value);
	}

	@Override
	public String getPlayer2() throws RemoteException {
		String player2 = this.gameServer.getPlayer2();
		setPlayer2(player2);
		return player2;
	}

	@Override
	public void setPlayer2(String value) throws RemoteException {
		this.gameBoard.setPlayer2(value);
	}

	@Override
	public GameStatus getStatus() throws RemoteException {
		return gameServer.getStatus();
	}

	@Override
	public void setStatus(GameStatus status) throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public int getId() {
		return id;
	}

	/**
	 * Startet das Spiel und zeigt das GUI an
	 */
	@Override
	public void Play() throws RemoteException {
		HashMap<String, Boolean> map = null;
		try{
			map = this.gameServer.getCells();
		} catch (ConnectException e) {
			reconnectWithWindow();
			map = this.gameServer.getCells();
		}
		for(String key : map.keySet()){
			gameBoard.setValue(key, map.get(key));
		}
		this.gameBoard.start();
		setNextPlayer(this.gameServer.getNextPlayer());
		getPlayer1();
		getPlayer2();
	}

	@Override
	public void Stop() throws RemoteException {
		gameBoard.stop();
		timer.stop();
	}

	@Override
	public void SwitchPlayer() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getWinner() throws RemoteException {
		return gameServer.getWinner();
	}
	
	public void testConntection() throws RemoteException {
		try {
			HashMap<String, Boolean> map = gameServer.getCells();
			for(String key : map.keySet()){
				gameBoard.setValue(key, map.get(key));
			}
		} catch (ConnectException e) {
			reconnectWithWindow();
			Play();
		}
	}

	private void reconnectWithWindow() throws RemoteException {
//		ReconnectWindow reconnectWindow = new ReconnectWindow(gameBoard.getFrame());
//		reconnectWindow.start();
		reconnect();
//		reconnectWindow.destroy();
		/*
		final ReconnectWindow reconnectWindow = new ReconnectWindow(gameBoard.getFrame());
		SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() throws Exception {
				publish(1);
				reconnect();
				return null;
			}

			@Override
			protected void process(List<Integer> chunks) {
				reconnectWindow.setVisible(true);
			}

			@Override
			protected void done() {
				reconnectWindow.dispose();
			}
		};
		worker.execute();
		*/
	}
	
	private void reconnect() throws RemoteException {
		try {
			IServer	server = ServerFinder.getServer(null);
			gameServer = ServerFinder.getGameServer(player);
			UnicastRemoteObject.unexportObject(this, true);
			IGame gameStub = (IGame) UnicastRemoteObject.exportObject(this,0);
			server.addClientGame(getId(), player, gameStub);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
	

	public void initReloadTimer() {
		timer = new Timer(3000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gameServer.getStatus();
				} catch (RemoteException e1) {
					try {
						reconnect();
					} catch (RemoteException e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		timer.start();
	}

	@Override
	public void reloadServerList() throws RemoteException {
		try {
			ServerFinder.reloadServerList();
		} catch (Exception e) {
			if(e instanceof RemoteException)
				throw (RemoteException)e;
			throw new RemoteException(e.getMessage());
		}
	}
}
