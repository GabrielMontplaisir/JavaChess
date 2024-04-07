package chess;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.ArrayDeque;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import gui.Board;

public class Move {
	private final String RANK = "abcdefgh";
	private final Color HIGHLIGHT = new Color(255, 240, 190);
	private String special = "";
	private Square last, current;
	private Piece lastPiece, currentPiece;
	private final JLabel moveLabel = new JLabel();
	private String coord;
	
	Move(final Square last, final Square current) {
		this.last = last;
		this.current = current;
		this.lastPiece = this.last.getPiece();
		this.currentPiece = this.current.getPiece();
		
		if (this.lastPiece instanceof Pawn && ((Pawn) this.lastPiece).isSecondMove()) {((Pawn) this.lastPiece).setSecondMove(false);}
		if (this.lastPiece.isFirstMove()) {		
			this.lastPiece.notFirstMove();
			if (this.lastPiece instanceof Pawn) {((Pawn) this.lastPiece).setSecondMove(true);}
		}
		
		// If player is in check
		if (this.lastPiece.getOwner().isKingChecked() && !(this.lastPiece instanceof King)) {
			this.lastPiece.getOwner().setChecked(false);
			this.lastPiece.getOwner().getAttackingPieces().clear();
			Square kingSquare = null;
			for (Piece p : this.lastPiece.getOwner().getPlayerPieces().keySet()) {
				if (p instanceof King) {
					kingSquare = this.lastPiece.getOwner().getPlayerPieces().get(p);
					break;
				}
			}
			kingSquare.setCurrentBGColour(kingSquare.getOriginalBGColour());
			kingSquare.getBtn().setBackground(kingSquare.getCurrentBGColour());
		}
		
		// If this is a pawn		
		if (this.lastPiece instanceof Pawn) {
			
			// If En Passant		
			if (((Pawn) this.lastPiece).getEnPassant() != null) {
				if (this.current.getY() != this.last.getY()) {
					((Pawn) this.lastPiece).getEnPassant().getPiece().setCaptured(((Pawn) this.lastPiece).getEnPassant().getPiece(), true);
					this.setRankAndFile(RANK.charAt(this.last.getY())+"x", null);
					((Pawn) this.lastPiece).getEnPassant().setPiece(null);
				}
				((Pawn) this.lastPiece).setEnPassant(null);
				finishMove(this.lastPiece);
				return;
			}
			
			// If Promotion		
			if ((!((Pawn) this.lastPiece).getTopDown() && this.current.getX() == 0) || (((Pawn) this.lastPiece).getTopDown() && this.current.getX() == 7)) {
				JOptionPane.showOptionDialog(null, null, null, JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, ((Pawn) this.lastPiece).getPromotionOptions(), null);
				if (((Pawn) this.lastPiece).getPromotionPiece() == null) return;
				
				if (this.currentPiece != null && this.currentPiece.getOwner().isLightPieces() != this.lastPiece.getOwner().isLightPieces()) {
					this.currentPiece.setCaptured(this.currentPiece, true);
					this.special = (Main.boardReversed) ? RANK.charAt(this.last.getY())+"x" : RANK.charAt(7-this.last.getY())+"x";
				}
				
				this.setRankAndFile(this.special, "="+((Pawn)this.lastPiece).getPromotionPiece().getName());
				this.lastPiece.getOwner().calculatePoints(((Pawn) this.lastPiece).getPromotionPiece().getPointValue());
				this.lastPiece.getOwner().getPlayerPieces().remove(this.lastPiece);
				this.lastPiece.getOwner().getPlayerPieces().put(((Pawn) this.lastPiece).getPromotionPiece(), this.last);
				finishMove(((Pawn) this.lastPiece).getPromotionPiece());
				return;
			}
			
			// If capture with pawn -- Not en Passant and not promotion
			if (this.currentPiece != null && this.currentPiece.getOwner().isLightPieces() != this.lastPiece.getOwner().isLightPieces()) {
				this.currentPiece.setCaptured(this.currentPiece, true);
				this.special = (Main.boardReversed) ? RANK.charAt(this.last.getY())+"x" : RANK.charAt(7-this.last.getY())+"x";
			}
			
			this.setRankAndFile(this.special, null);
			finishMove(this.lastPiece);
			return;
		}
		
		// If regular piece captures
		if (this.currentPiece != null && this.currentPiece.getOwner().isLightPieces() != this.lastPiece.getOwner().isLightPieces()) {
			this.currentPiece.setCaptured(this.currentPiece, true);
			this.special = "x";
		}
		
		this.setRankAndFile(this.lastPiece.getName()+this.special, null);
		
		// If this is a king
		if (this.lastPiece instanceof King) {
			this.lastPiece.getOwner().setChecked(false);
			this.lastPiece.getOwner().getOpponent().getCoveredLines().clear();
			this.lastPiece.getOwner().getAttackingPieces().clear();
			// If it's a left castling move			
			if (((King) this.lastPiece).leftCastlePossible() && this.last.getY()-this.current.getY() == 2) {
				Board.boardArray[this.current.getX()][this.current.getY()+1].setPiece(((King) this.lastPiece).getLeftCastlingRook().getPiece());
				this.lastPiece.getOwner().getPlayerPieces().replace(((King) this.lastPiece).getLeftCastlingRook().getPiece(), Board.boardArray[this.current.getX()][this.current.getY()+1]);
				this.coord = (Main.boardReversed) ? "O-O-O" : "O-O";
				((King) this.lastPiece).getLeftCastlingRook().setPiece(null);
			
			// If it's a right castling move
			} else if (((King) this.lastPiece).rightCastlePossible() && this.last.getY()-this.current.getY() == -2) {
				Board.boardArray[this.current.getX()][this.current.getY()-1].setPiece(((King) this.lastPiece).getRightCastlingRook().getPiece());
				this.lastPiece.getOwner().getPlayerPieces().replace(((King) this.lastPiece).getRightCastlingRook().getPiece(), Board.boardArray[this.current.getX()][this.current.getY()-1]);
				this.coord = (Main.boardReversed) ? "O-O" : "O-O-O";
				((King) this.lastPiece).getRightCastlingRook().setPiece(null);
			}
			((King) this.lastPiece).notCastlingMove();
		}
		
		finishMove(this.lastPiece);
		
	}
	
	public Square getLast() {return this.last;}
	public Square getCurrent() {return this.current;}
	public String getCoord() {return this.coord;}
	
	private void setRankAndFile(String special, String last) {
		if (special == null) special = "";
		if (last == null) last = "";
		this.coord = (Main.boardReversed) ? special+RANK.charAt(this.current.getY())+(8-this.current.getX())+last : special+RANK.charAt(7-this.current.getY())+(this.current.getX()+1)+last;
	}
	
	public void revertTileColours() {
		this.last.setCurrentBGColour(this.last.getOriginalBGColour());
		this.last.getBtn().setBackground(this.last.getOriginalBGColour());
		this.current.setCurrentBGColour(this.current.getOriginalBGColour());
		this.current.getBtn().setBackground(this.current.getOriginalBGColour());
	}
	
	public void setTileColours() {
		this.last.setCurrentBGColour(this.HIGHLIGHT);
		this.last.getBtn().setBackground(this.last.getCurrentBGColour());
		this.current.setCurrentBGColour(this.HIGHLIGHT);
		this.current.getBtn().setBackground(this.current.getCurrentBGColour());
	}
	
	private void finishMove(Piece piece) {
		Main.setLastMove(this);
		this.current.setPiece(piece);
		this.currentPiece = this.current.getPiece();
		// Remove the last square's piece.		
		this.last.setPiece(null);
		this.lastPiece = this.last.getPiece();
		
		this.currentPiece.getOwner().getPlayerPieces().replace(this.currentPiece, this.current);
		this.currentPiece.getOwner().refreshMoves();
		
		// Verify if this puts the opponent's king in check
		this.opponentKingChecked();
		
		// Update the opponent's valid moves and check if checkmate.
		this.currentPiece.getOwner().getOpponent().refreshMoves();
		boolean checkmated = verifyCheckmate();
		
		// Change players once a legal move was made.		
		if (!checkmated) Main.swapTurns();
		
		// Update Move Panel text		
		this.updateMovePanel();
	}
	
/*
 * VERIFY IF MOVE CHECKS OPPONENT KING
 * 
 * Find's the opponent's King, then finds the attacking piece's "line" containing the king.
 * Sets the player as checked, then adds the piece / line to a HashMap.
 * 
 * Update the checked King to have a red background.
 */
	
	private void opponentKingChecked() {
		if (this.currentPiece.getOwner().getOpponent().isKingChecked()) return;
		
		for (Piece p : this.currentPiece.getOwner().getPlayerPieces().keySet()) {
			
			// Find opponent's king
			Optional<Square> opponentKingSquare = p.getCoveringSquares().stream().filter(sq -> sq.getPiece() instanceof King && sq.getPiece().getOwner().isLightPieces() != p.getOwner().isLightPieces()).findFirst();
			if (opponentKingSquare.isPresent()) {
				
				// Find the appropriate "line" containing the king. Add the piece's current square.
				Optional<ArrayDeque<Square>> attackingLine = p.getPossibleMoves().stream().filter(line -> line.contains(opponentKingSquare.get())).findFirst();
				attackingLine.get().add(this.currentPiece.getOwner().getPlayerPieces().get(p));
				
				// Set Player as checked, and add the piece as an attacking piece along with the "line".
				opponentKingSquare.get().getPiece().getOwner().setChecked(true);
				opponentKingSquare.get().getPiece().getOwner().getAttackingPieces().put(p, attackingLine.get());
				
				// Indicate King is checked. Set background to red.			
				opponentKingSquare.get().setCurrentBGColour(Color.red);
				opponentKingSquare.get().getBtn().setBackground(opponentKingSquare.get().getCurrentBGColour());
				this.coord += "+";
			}
		}
	}
	
/*
 * VERIFY IF OPPONENT PLAYER IS CHECKMATED
 * If the total collective moves for the opponent player is 0 (or less, which is impossible, but set for good measure), then the player is checkmated.
 */
	
	private boolean verifyCheckmate() {
		if (!this.currentPiece.getOwner().getOpponent().isKingChecked()) return false;
		
		if (this.currentPiece.getOwner().getOpponent().getCollectiveMoves().size() <= 0) {
			this.coord = this.coord.replace("+", "#");
			System.out.println("Checkmate!");
			return true;
		}
		
		return false;
	}
	
/*
 * UPDATE THE MOVE PANEL
 */
	
	private void updateMovePanel() {
		this.moveLabel.setText(this.coord);	
		Main.cst.gridx = (this.currentPiece.getOwner().isLightPieces()) ? 1 : 2;
		Main.cst.ipadx = 50;
		Main.cst.gridy = Main.getTurnCount();
		Main.cst.ipady = 10;
		Main.cst.anchor = GridBagConstraints.BASELINE_LEADING;
		Main.movePanel.add(moveLabel, Main.cst);
	}
}
