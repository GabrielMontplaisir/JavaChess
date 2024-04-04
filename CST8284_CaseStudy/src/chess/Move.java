package chess;

import java.awt.Color;
import java.awt.GridBagConstraints;

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
	private Square last;
	private Square current;
	private final JLabel moveLabel = new JLabel();
	private String coord;
	
	Move(final Square last, final Square current) {
		this.last = last;
		this.current = current;
		
		if (this.last.getPiece() instanceof Pawn && ((Pawn) this.last.getPiece()).isSecondMove()) {((Pawn) this.last.getPiece()).setSecondMove(false);}
		if (this.last.getPiece().isFirstMove()) {		
			this.last.getPiece().notFirstMove();
			if (this.last.getPiece() instanceof Pawn) {((Pawn) this.last.getPiece()).setSecondMove(true);}
		}
		
		// If this is a pawn		
		if (this.last.getPiece() instanceof Pawn) {
			
			// If En Passant		
			if (((Pawn) this.last.getPiece()).getEnPassant() != null) {
				if (this.current.getY() != this.last.getY()) {
					((Pawn) this.last.getPiece()).getEnPassant().getPiece().setCaptured(((Pawn) this.last.getPiece()).getEnPassant().getPiece(), true);
					this.setRankAndFile(RANK.charAt(this.last.getY())+"x", null);
					((Pawn) this.last.getPiece()).getEnPassant().setPiece(null);
				}
				((Pawn) this.last.getPiece()).setEnPassant(null);
				finishMove(this.last.getPiece());
				return;
			}
			
			// If Promotion		
			if ((!((Pawn) this.last.getPiece()).getTopDown() && this.current.getX() == 0) || (((Pawn) this.last.getPiece()).getTopDown() && this.current.getX() == 7)) {
				JOptionPane.showOptionDialog(null, null, null, JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE, null, ((Pawn) this.last.getPiece()).getPromotionOptions(), null);
				if (((Pawn) this.last.getPiece()).getPromotionPiece() == null) return;
				
				if (this.current.getPiece() != null && this.current.getPiece().getOwner().isLightPieces() != this.last.getPiece().getOwner().isLightPieces()) {
					this.current.getPiece().setCaptured(this.current.getPiece(), true);
					this.special = (Main.boardReversed) ? RANK.charAt(this.last.getY())+"x" : RANK.charAt(7-this.last.getY())+"x";
				}
				
				this.setRankAndFile(this.special, "="+((Pawn)this.last.getPiece()).getPromotionPiece().getName());
				this.last.getPiece().getOwner().calculatePoints(((Pawn) this.last.getPiece()).getPromotionPiece().getPointValue());
				Board.pieces.remove(this.last.getPiece());
				Board.pieces.put(((Pawn) this.last.getPiece()).getPromotionPiece(), this.last);
				finishMove(((Pawn) this.last.getPiece()).getPromotionPiece());
				return;
			}
			
			// If capture with pawn -- Not en Passant and not promotion
			if (this.current.getPiece() != null && this.current.getPiece().getOwner().isLightPieces() != this.last.getPiece().getOwner().isLightPieces()) {
				this.current.getPiece().setCaptured(this.current.getPiece(), true);
				this.special = (Main.boardReversed) ? RANK.charAt(this.last.getY())+"x" : RANK.charAt(7-this.last.getY())+"x";
			}
				
			this.setRankAndFile(this.special, null);
			finishMove(this.last.getPiece());
			return;
		}
		
		// If regular piece captures
		if (this.current.getPiece() != null && this.current.getPiece().getOwner().isLightPieces() != this.last.getPiece().getOwner().isLightPieces()) {
			this.current.getPiece().setCaptured(this.current.getPiece(), true);
			this.special = "x";
		}
		
		this.setRankAndFile(this.last.getPiece().getName()+this.special, null);
		
		// If this is a king
		if (this.last.getPiece() instanceof King) {
			this.last.getPiece().getOwner().setChecked(false);
			// If it's a left castling move			
			if (((King) this.last.getPiece()).leftCastlePossible() && this.last.getY()-this.current.getY() == 2) {
				Board.boardArray[this.current.getX()][this.current.getY()+1].setPiece(((King) this.last.getPiece()).getLeftCastlingRook().getPiece());
				Board.pieces.replace(((King) this.last.getPiece()).getLeftCastlingRook().getPiece(), Board.boardArray[this.current.getX()][this.current.getY()+1]);
				this.coord = (Main.boardReversed) ? "O-O-O" : "O-O";
				((King) this.last.getPiece()).getLeftCastlingRook().setPiece(null);
			
			// If it's a right castling move
			} else if (((King) this.last.getPiece()).rightCastlePossible() && this.last.getY()-this.current.getY() == -2) {
				Board.boardArray[this.current.getX()][this.current.getY()-1].setPiece(((King) this.last.getPiece()).getRightCastlingRook().getPiece());
				Board.pieces.replace(((King) this.last.getPiece()).getRightCastlingRook().getPiece(), Board.boardArray[this.current.getX()][this.current.getY()-1]);
				this.coord = (Main.boardReversed) ? "O-O" : "O-O-O";
				((King) this.last.getPiece()).getRightCastlingRook().setPiece(null);
			}
			((King) this.last.getPiece()).notCastlingMove();
		}
		
		finishMove(this.last.getPiece());
		
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
		this.last.setPiece(null);
		
		Board.pieces.replace(this.current.getPiece(), this.current);
		Board.refreshMoves();
		
		
		// If the piece attacks the king, then set the king in check.
		this.current.getPiece().getValidMoves().forEach(sq -> {
			if (sq.getPiece() != null && sq.getPiece().getOwner().isLightPieces() != this.current.getPiece().getOwner().isLightPieces() && sq.getPiece() instanceof King) {
				sq.getPiece().getOwner().setChecked(true);
				this.coord += "+";
			}
		});
		
		this.moveLabel.setText(this.coord);
		
		Main.swapTurns();
		
		Main.cst.gridx = (this.current.getPiece().getOwner().isLightPieces()) ? 1 : 2;
		Main.cst.ipadx = 50;
		Main.cst.gridy = Main.getTurnCount();
		Main.cst.ipady = 10;
		Main.cst.anchor = GridBagConstraints.BASELINE_LEADING;
		Main.movePanel.add(moveLabel, Main.cst);
	}
}
