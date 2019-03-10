package gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReconnectWindow extends Thread{

	private JPanel contentPane;
	private int i = 1;
	private JDialog dialog;
	private Timer timer;

	/**
	 * Create the frame.
	 */
	public ReconnectWindow(Component parent) {
		dialog = new JDialog();
		dialog.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		dialog.setUndecorated(true);
		dialog.setModal(true);
//		dialog.setLocationRelativeTo(parent);
//		dialog.setAlwaysOnTop(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		width = width / 2 - 110;
		height = height / 2 - 35;
		dialog.setBounds((int) width, (int) height, 220, 70);
		this.contentPane = new JPanel();
		this.contentPane.setBackground(new Color(255, 215, 0));
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		dialog.setContentPane(this.contentPane);

		final JLabel lblLade = new JLabel("Reconnect...");
		lblLade.setHorizontalAlignment(SwingConstants.CENTER);
		lblLade.setFont(new Font("Times New Roman", Font.PLAIN, 23));
		this.contentPane.add(lblLade, BorderLayout.CENTER);

		timer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (ReconnectWindow.this.i) {
					case 0:
						lblLade.setText("Reconnect");
						ReconnectWindow.this.i = 1;
						break;
					case 1:
						lblLade.setText("Reconnect.");
						ReconnectWindow.this.i = 2;
						break;
					case 2:
						lblLade.setText("Reconnect..");
						ReconnectWindow.this.i = 3;
						break;
					case 3:
						lblLade.setText("Reconnect...");
						ReconnectWindow.this.i = 0;
						break;
				}
			}
		});
		timer.start();
		dialog.setModal(false);
		dialog.getGlassPane().setCursor(Cursor.getDefaultCursor());
	}
	
	@Override
	public void run() {
		dialog.setVisible(true);
	}
}
