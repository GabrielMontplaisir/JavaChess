package chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayDeque;

import javax.swing.JLabel;
import javax.swing.JPanel;

import chess.pieces.Piece;

public class Player {
	
	// UI Components for Player	
	private final JPanel playerBox = new JPanel(new BorderLayout(0,0));
	private final JLabel playerTitle = new JLabel("BLACK");
	private final JLabel pointDiff = new JLabel();
	private final JPanel capturedPiecesArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
	private boolean lightPieces = false;
	private final HashMap<Piece, Square> playerPieces = new HashMap<Piece, Square>();
	private final HashMap<Piece, ArrayDeque<Square>> attackingPieces = new HashMap<Piece, ArrayDeque<Square>>();
	private final HashMap<Piece, ArrayDeque<Square>> coveringPieces = new HashMap<Piece, ArrayDeque<Square>>();
	private final HashSet<Square> collectiveMoves = new HashSet<Square>();
	private boolean playerTurn = false;
	private boolean kingChecked = false;
	private Player opponent;
	private Square selection;
	private int pointTotal;
	
	public boolean isLightPieces() {return this.lightPieces;}
	public HashMap<Piece, Square> getPlayerPieces() {return this.playerPieces;}
	public HashMap<Piece, ArrayDeque<Square>> getAttackingPieces() {return this.attackingPieces;}
	public HashMap<Piece, ArrayDeque<Square>> getCoveredLines() {return this.coveringPieces;}
	public HashSet<Square> getCollectiveMoves() {return this.collectiveMoves;}
	public boolean isKingChecked() {return this.kingChecked;}
	public boolean isPlayerTurn() {return this.playerTurn;}
	public Player getOpponent() {return this.opponent;}
	public Square getSelection() {return this.selection;}
	public int getPointTotal() {return this.pointTotal;}
	public JPanel getCapturedPiecesArea() {return this.capturedPiecesArea;}
	
	public void setLightPieces(boolean isWhite) {
		this.lightPieces = isWhite;
		if (this.lightPieces) {
			playerTitle.setText("WHITE");
		}
	}
	
	public void addToGUI(JPanel gui, String position) {
		playerBox.setPreferredSize(new Dimension(gui.getWidth(), 40));
		playerBox.add(playerTitle, BorderLayout.PAGE_START);
		
		capturedPiecesArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		capturedPiecesArea.add(pointDiff);
		
		playerBox.add(capturedPiecesArea, BorderLayout.PAGE_END);
		gui.add(playerBox, position);
	}
	
	public void setChecked(boolean isChecked) {
		this.kingChecked = isChecked;
	}
	
	public void setPlayerTurn(boolean turn) {
		this.playerTurn = turn;
	}
	
	public void calculatePoints(int points) {
		this.pointTotal += points;
		int diff = this.pointTotal - this.getOpponent().getPointTotal();
		this.getOpponent().setPointLabel(this.getOpponent().getPointTotal() - this.getPointTotal());
		this.setPointLabel(diff);
	}
	
	public void setPointLabel(int diff) {
		if (diff > 0) {
			pointDiff.setText("+"+diff);
		} else {
			pointDiff.setText(null);
		}
	}
	
	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}
	
	private void resetSquareBG(Square sq) {
		sq.getBtn().setBackground(sq.getCurrentBGColour());
		if (sq.getPiece() != null) {sq.getPiece().deHighlightMoves();}
	}
	
	public void refreshMoves() {
		for (Piece piece : this.playerPieces.keySet()) {
			piece.clearMoves();
			piece.findMoves(this.playerPieces.get(piece));
		}
		
		this.collectiveMoves.clear();
		for (Piece piece : this.playerPieces.keySet()) {
			this.collectiveMoves.addAll(piece.getValidSquares());
		}
	}
	
	public void setSelection(Square square) {
		// If player has no selection
		if (this.selection == null) {
			this.selection = square;
			
		// If player selects same square
		} else if (this.selection == square) {
			this.resetSquareBG(this.selection);
			this.selection = null;
			return;
		
		// Otherwise, set new selection
		} else {
			
			// If selection is highlighted, move piece
			if (this.selection.getPiece() != null && this.selection.getPiece().getValidSquares().contains(square) && Main.getCurrentPlayer().isLightPieces() == this.selection.getPiece().getOwner().isLightPieces()) {
					new Move(this.selection, square);				
				return;
			}
						
			this.resetSquareBG(this.selection);
			this.selection = square;
		}

		this.selection.getBtn().setBackground(Color.yellow);
		
		// If new selection is a piece
		if (this.selection.getPiece() != null && Main.getCurrentPlayer().isLightPieces() == this.selection.getPiece().getOwner().isLightPieces()) {
			System.out.println(this.selection);
			if (this.selection.getPiece() != null) System.out.println(this.getCoveredLines().get(this.selection.getPiece()));
			this.selection.getPiece().highlightMoves();				
		} else {
			if (this.selection.getPiece() != null && Main.getCurrentPlayer().isLightPieces() != this.selection.getPiece().getOwner().isLightPieces()) this.selection.getPiece().getCoveringSquares().forEach(sq -> sq.getBtn().setBackground(Color.gray));
			System.out.println(this.selection);
			if (this.selection.getPiece() != null) System.out.println(this.getCoveredLines().get(this.selection.getPiece()));
		}
	}
	
}
