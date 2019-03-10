package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.swing.JButton;

import java.awt.Font;

import net.miginfocom.swing.MigLayout;
import object.GameClient;

public class GameBoard {

	private JFrame frame;
	private int gameSize;
	private HashMap<String, JButton> mapButton;
	private boolean myTurn;
	private GameClient game;
	private JLabel labelPlayer1;
	private JLabel labelPlayer2;
	private JLabel labelNext;
	private JLabel labelWinner;

	/**
	 * Create the application.
	 */
	public GameBoard(int gameSize, GameClient game) {
		this.gameSize = gameSize;
		this.game = game;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[20][grow][20]"));
		
		JPanel panelHeader = new JPanel();
		frame.getContentPane().add(panelHeader, "cell 0 0,grow");
		
		labelPlayer1 = new JLabel();
		labelPlayer2 = new JLabel();
		labelNext = new JLabel();
		
		JLabel labelVS = new JLabel(" vs. ");
		JLabel labelNextText = new JLabel(" Next: ");
		panelHeader.add(labelPlayer1);
		panelHeader.add(labelVS);
		panelHeader.add(labelPlayer2);
		panelHeader.add(labelNextText);
		panelHeader.add(labelNext);
		
		JPanel panelGame = new JPanel();
		panelGame.setLayout(new GridLayout(this.gameSize, this.gameSize, 1, 1));
		addButtons(panelGame);
		frame.getContentPane().add(panelGame, "cell 0 1,grow");
		
		JPanel panelButton = new JPanel();
		frame.getContentPane().add(panelButton, "cell 0 2,grow");
		
		labelWinner = new JLabel();
		panelButton.add(labelWinner);
		
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Main.logout();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}
	
	private void addButtons(JPanel panelGame) {
		mapButton = new HashMap<String, JButton>();
		ActionListener action = getActionListener();
		for(int i = 1; i<= this.gameSize; i++){
			for(int j = 1; j <= this.gameSize; j++){
				String key = i+","+j;
				JButton button = new JButton();
				button.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				button.setAlignmentY(JLabel.CENTER_ALIGNMENT);
				button.setActionCommand(key);
				button.addActionListener(action);
				button.setFont(new Font("Arial", Font.BOLD, 30));
				mapButton.put(key, button);
				panelGame.add(button);
			}
		}
	}

	/**
	 * Der Actiopn Listener hängt an allen buttons und schickt dem Game welches Feld angeklickt wurde. 
	 * Das passiert nur, wenn die Variable myTurn == true ist.
	 * @return
	 */
	private ActionListener getActionListener() {
		ActionListener actionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = ((JButton)e.getSource()).getText();
				if(myTurn && text.isEmpty()){
					String key = e.getActionCommand();
					try {
						setValue(key, game.setCell(key));
						game.Play();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					try {
						game.testConntection();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		};
		return actionListener;
	}
	
	/**
	 * Schreibt ein X wenn @param value == true und für false ein O. Falls value null ist wird das Feld geleert.
	 * @param key
	 * @param value
	 */
	public void setValue(String key, Boolean value) {
		JButton button = mapButton.get(key);
		if(value == null){
			button.setText("");
		} else {
			if(value){
				button.setText("X");
			} else {
				button.setText("O");
			}
		}
	}
	
	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}
	
	public void start() {
		this.frame.setVisible(true);
	}
	
	public void stop() throws RemoteException {
		if(this.game.getWinner() == null)
			labelWinner.setText("Ende: No winner ");
		else
			labelWinner.setText("Ende: Winner => " + this.game.getWinner());
	}
	
	public void setPlayer1(String player1) {
		this.labelPlayer1.setText(player1);
	}

	public void setPlayer2(String player2) {
		this.labelPlayer2.setText(player2);
	}
	
	public void setNextPlayer(String player) {
		this.labelNext.setText(player);
	}
	
	public void setPlayer(String player) {
		frame.setTitle(player);
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
}
