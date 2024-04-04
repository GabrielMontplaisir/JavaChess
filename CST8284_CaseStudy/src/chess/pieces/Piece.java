package chess.pieces;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayDeque;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import chess.Main;
import chess.Player;
import chess.Square;
import gui.Board;

// Chess Pieces icons from https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent

public abstract class Piece {
	private Player owner;
	protected ArrayDeque<Square> validMoves = new ArrayDeque<Square>();
	protected boolean firstMove = true;
	private boolean isCaptured = false;
	private int pointValue;
	private Image image;
	private String name;
	
	// Constructor	
	
	Piece(final Player owner, final int pointValue, final String name) {
		this.owner = owner;
		this.pointValue = pointValue;
		this.name = name;
		if (this.owner.isLightPieces()) {
			try {
				this.image = ImageIO.read(this.getClass().getResource("/light_"+this.name.toLowerCase()+".png"));
			} catch (IOException e) {
				System.out.println(e);
			}
		} else {
			try {
				this.image = ImageIO.read(this.getClass().getResource("/dark_"+this.name.toLowerCase()+".png"));
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		
	}
	
	// Getter Methods	
	
	public Player getOwner() {return this.owner;}
	public boolean isFirstMove() {return this.firstMove;}
	public boolean isCaptured() {return this.isCaptured;}
	public Image getImage() {return this.image;}
	public String getName() {return this.name;}
	public ArrayDeque<Square> getValidMoves() {return this.validMoves;}
	public int getPointValue() {return this.pointValue;}
	
	public void setCaptured(Piece piece, boolean isCaptured) {
		this.isCaptured = isCaptured;
		Main.getCurrentPlayer().calculatePoints(this.pointValue);
		Main.getCurrentPlayer().getCapturedPiecesArea().add(new JLabel(new ImageIcon(piece.image.getScaledInstance(20, 20, Image.SCALE_DEFAULT))));
		Board.pieces.remove(this);
	}
	
	public void notFirstMove() {
		this.firstMove = false;
	}
		
	public void clearMoves() {
		this.deHighlightMoves();		
		this.validMoves.removeAll(this.validMoves);
	}
	
	protected ArrayDeque<Square> globalValidation(ArrayDeque<Square> array) {
		ArrayDeque<Square> temp = new ArrayDeque<Square>();
		
		return temp;
	}
	
	public void highlightMoves() {				
		this.validMoves.forEach((sq) -> {
			sq.getBtn().setBackground(Color.CYAN);
		});
		
	}
	
	public void deHighlightMoves() {				
		this.validMoves.forEach((el) -> {
			el.getBtn().setBackground(el.getCurrentBGColour());
		});
	}
	
	private boolean findOpponentPiece(Square sq) {
		return sq.getCoveringPieces().stream().anyMatch(csq -> csq.getOwner().isLightPieces() != this.getOwner().isLightPieces());
	}
	
	public abstract ArrayDeque<Square> findMoves(Square current);
	
	public void validateMoves(ArrayDeque<Square> array) {
		
		array.forEach((sq) -> {
			if (sq.getX() >= 0 && sq.getX() <= 7 && sq.getY() >= 0 && sq.getY() <= 7) {

				if (Board.boardArray[sq.getX()][sq.getY()].getPiece() == null || Board.boardArray[sq.getX()][sq.getY()].getPiece().getOwner().isLightPieces() != this.getOwner().isLightPieces()) {
					boolean coveredSquare = findOpponentPiece(Board.boardArray[sq.getX()][sq.getY()]);
					
					// TODO: Check if piece is NOT king, OR if (piece IS king AND NOT covered by opponent)
					if (this instanceof King && coveredSquare) return;
					this.validMoves.add(Board.boardArray[sq.getX()][sq.getY()]);
					Board.boardArray[sq.getX()][sq.getY()].getCoveringPieces().add(this);
				}
			}
		});

	}
}