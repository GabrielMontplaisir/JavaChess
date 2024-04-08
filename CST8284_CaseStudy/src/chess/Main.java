package chess;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import gui.Board;
import gui.Menu;
import gui.MovePanel;

public class Main {
	private final static JPanel gui = new JPanel(new BorderLayout());
	private final static MovePanel movePanel = new MovePanel(gui);
	private final Player p1 = new Player();
	private final Player p2 = new Player();
	private static Player currentPlayer;
	private static boolean boardReversed;
	
// =================================== CONSTRUCTOR ===================================
	
	Main() {
		// Main Container Settings
		gui.setBorder(new EmptyBorder(10,20,20,20));
		
		this.p1.setOpponent(p2);
		this.p2.setOpponent(p1);
		
		this.p1.getPlayerBox().addToGUI(gui, BorderLayout.PAGE_END);
		this.p2.getPlayerBox().addToGUI(gui, BorderLayout.PAGE_START);
		
		final Board board = new Board(gui);
		
		// Setup game
		this.setupGame(board);
	}
	
// =================================== GETTER METHODS ===================================	
	
	public static boolean getBoardDirection() {return boardReversed;}
	public static Player getCurrentPlayer() {return currentPlayer;}
	public static MovePanel getMovePanel() {return movePanel;}
	
	
// =================================== SETTER METHODS ===================================
	
	private static void setCurrentPlayer(Player player) {
		currentPlayer = player;
	}
	
	private void setBoardReversed(boolean isReversed) {
		this.boardReversed = isReversed;
	}
	
// =================================== OTHER METHODS ===================================
	
/*
 * GAME SETUP
 * Resets turn cout, randomizes which player starts as light pieces, and sets the current player.
 *  
 */
	
	private void setupGame(Board board) {
		movePanel.setTurnCount(0);
		
		if (Math.random() >= 0.5) {
			setCurrentPlayer(p1);
			this.setBoardReversed(true);
		} else {
			setCurrentPlayer(p2);
			this.setBoardReversed(false);
		}
		
		currentPlayer.setLightPieces(true);
		currentPlayer.setPlayerTurn(true);
		
		board.resetBoard(p1, p2, this.getBoardDirection());
	}
	
	
/*
 * SWAP TURNS
 * This must happen AFTER updateMovePanel() as the listMoves checks the algebraic notation to see if there's repetition more than three times.
 *  
 */
	
	public static void swapTurns() {
		currentPlayer.getOpponent().setPlayerTurn(true);
		currentPlayer.setPlayerTurn(false);
		setCurrentPlayer(currentPlayer.getOpponent());
		currentPlayer.refreshMoves();
	}
	


	
// =================================== MAIN METHOD ===================================	
	
	public static void main(String[] args) {
		// Create window and add window menu
		final JFrame main = new JFrame("CST8284 Chess by Gabriel Montplaisir -- 041125807");
		new Menu(main);
		
		// Set window default settings
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setSize(1200,800);
		
		// Add GUI container and make frame visible		
		main.add(gui);
		main.setVisible(true);
	}

}
