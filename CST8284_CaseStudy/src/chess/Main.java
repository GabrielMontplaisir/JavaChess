package chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import gui.Board;
import gui.Menu;

public class Main {
	private final Player p1 = new Player();
	private final Player p2 = new Player();
	public static final JPanel movePanel = new JPanel(new GridBagLayout());
	public static final GridBagConstraints cst = new GridBagConstraints();
	private static int turnCount= -1;
	private static Move lastMove;
	private static Player currentPlayer;
	public static boolean boardReversed;
	
	Main() {
		p1.setOpponent(p2);
		p2.setOpponent(p1);
		
		if (Math.random() >= 0.5) {
			setCurrentPlayer(p1);
			boardReversed = true;
		} else {
			setCurrentPlayer(p2);
			boardReversed = false;
		}

		incrementTurn();
		currentPlayer.setLightPieces(true);
		currentPlayer.setPlayerTurn(true);
	}
	
	public boolean getBoardDirection() {return boardReversed;}
	public static int getTurnCount() {return turnCount;}
	public static Player getCurrentPlayer() {return currentPlayer;}
	public static Move getLastMove() {return lastMove;}
	
	private static void setCurrentPlayer(Player player) {
		currentPlayer = player;
	}
	
	private static void incrementTurn() {
		turnCount += 1;
		
		cst.anchor = GridBagConstraints.PAGE_START;
		cst.gridx = 0;
		cst.gridy = turnCount;
		cst.ipadx = 50;
		cst.ipady = 10;
		if (turnCount > 0) movePanel.add(new JLabel(turnCount+"."), cst);
	}
	
	public static void setLastMove(Move move) {
		if (lastMove != null) {
			lastMove.revertTileColours();
		};
		
		lastMove = move;
		lastMove.setTileColours();
	}
	
	public static void swapTurns() {
		if (currentPlayer.isLightPieces()) incrementTurn();
		currentPlayer.getOpponent().setPlayerTurn(true);
		currentPlayer.setPlayerTurn(false);
		setCurrentPlayer(currentPlayer.getOpponent());
	}
	
//	TODO: Create a title screen for game
//	final static void createTitleScreen() {
//		JFrame main = new JFrame("CST8284 Chess by Gabriel Montplaisir -- 041125807");
//		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		main.setSize(600, 600);
//		main.setLayout(new GridBagLayout());
//		main.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		
//		JPanel titleText = new JPanel();
//		JLabel title = new JLabel("<html><h1>Chess</h1></html>");
//		JLabel subtitle = new JLabel("<html><h2><i>By Gabriel Montplaisir</i></h2></html>");
//		
//		titleText.add(title);
//		titleText.add(subtitle);
//	
//		
//		JPanel menuBtns = new JPanel(new GridBagLayout());
//		
//		JButton newGameBtn = new JButton("New Game");
//		
//		newGameBtn.addActionListener(event -> {
//			JOptionPane.showMessageDialog(main, "You clicked New Game.");
//		});
//		
//		JButton exitBtn = new JButton("Exit");
//		
//		exitBtn.addActionListener(event -> { 
//			main.dispose();
//		});
//		
//		menuBtns.add(newGameBtn);
//		menuBtns.add(exitBtn);
//		
//		
//		
//		main.getContentPane().add(titleText);
//		main.getContentPane().add(menuBtns);
//		
//		main.setVisible(true);
//	}
	
	public final void launchGUI(JFrame main) {
		final Menu menu = new Menu(main);
		final JPanel gui = new JPanel(new BorderLayout());
		p1.addToGUI(gui, BorderLayout.PAGE_END);
		p2.addToGUI(gui, BorderLayout.PAGE_START);
		gui.setBorder(new EmptyBorder(10,20,20,20));
		movePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Moves"));
		movePanel.setPreferredSize(new Dimension(375, gui.getHeight()));
		gui.add(movePanel, BorderLayout.LINE_END);
		final Board board = new Board(gui, p1, p2);
		main.add(gui);
	}

	public static void main(String[] args) {
		final Main game = new Main();
		final JFrame main = new JFrame("CST8284 Chess by Gabriel Montplaisir -- 041125807");
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setSize(1200,800);
		game.launchGUI(main);
		
		main.setVisible(true);
//		gameScreen();
	}

}
