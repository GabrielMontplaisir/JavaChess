package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayDeque;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chess.Main;
import chess.Move;

public class MovePanel {
	private final JPanel panel = new JPanel(new GridBagLayout());
	final GridBagConstraints cst = new GridBagConstraints();
	private final ArrayDeque<String> moves = new ArrayDeque<String>();
	private Move lastMove;
	private int turnCount;
	
// =================================== CONSTRUCTOR ===================================
	
	public MovePanel(JPanel gui) {
		this.panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Moves"));
		this.panel.setPreferredSize(new Dimension(375, gui.getHeight()));
		gui.add(this.panel, BorderLayout.LINE_END);
	}
	
// =================================== GETTER METHODS ===================================		
	
	public int getTurnCount() {return this.turnCount;}
	public Move getLastMove() {return this.lastMove;}
	public ArrayDeque<String> getMoves() {return this.moves;}
	
// =================================== SETTER METHODS ===================================
	
/*
 * SET TURN COUNT
 */
	
	public void setTurnCount(int count) {
		this.turnCount += count;
	}
	
/*
 * UPDATE LAST MOVE
 * Will also update the background colour of the square to show the last move.
 */
	
	public void setLastMove(Move move) {
		if (this.lastMove != null) this.lastMove.revertTileColours();
		
		this.lastMove = move;
		this.lastMove.setTileColours();
	}

// =================================== OTHER METHODS ===================================

	private void addRowToPanel() {
		this.cst.anchor = GridBagConstraints.PAGE_START;
		this.cst.gridx = 0;
		this.cst.gridy = this.getTurnCount();
		this.cst.ipadx = 50;
		this.cst.ipady = 10;
		if (this.getTurnCount() > 0) this.panel.add(new JLabel(this.getTurnCount()+"."), cst);
	}
	
	private void addMoveToPanel(String notation, boolean isLightPieces) {
		JLabel label = new JLabel(notation);
		this.cst.gridx = (isLightPieces) ? 1 : 2;
		this.cst.ipadx = 50;
		this.cst.gridy = this.getTurnCount();
		this.cst.ipady = 10;
		this.cst.anchor = GridBagConstraints.BASELINE_LEADING;
		this.panel.add(label, this.cst);
	}
	
	public void updateLastMove(Move lastMove) {
		// Sets last move and highlights board tiles.
		this.setLastMove(lastMove);
		
		if (this.lastMove.getCurrentPiece().getOwner().isLightPieces()) {
			this.setTurnCount(this.getTurnCount() + 1);
			this.addRowToPanel();
		}
		
		this.moves.add(this.lastMove.getCoord());
		this.addMoveToPanel(this.lastMove.getCoord(), this.lastMove.getCurrentPiece().getOwner().isLightPieces());
		
		Main.swapTurns();
		
		
	}
	
}
