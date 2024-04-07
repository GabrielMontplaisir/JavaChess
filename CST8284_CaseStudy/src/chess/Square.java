package chess;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import chess.pieces.Piece;

public class Square {
	private final int x, y;
	private Piece piece;
	private final JButton btn = new JButton();
	private final Color originalBGColour;
	private Color currentBGColour;
	private final Color LIGHT_SQUARE = new Color(250, 250, 240);
	private final Color DARK_SQUARE = new Color(115, 140, 80);
	
	public Square(final int x, final int y) {
		this.x = x;
		this.y = y;
		if ((x % 2 == 0 && y % 2 == 0) || (x % 2 == 1 && y % 2 == 1)) {
			this.originalBGColour = LIGHT_SQUARE;
			this.currentBGColour = LIGHT_SQUARE;
		} else {
			this.originalBGColour = DARK_SQUARE;
			this.currentBGColour = DARK_SQUARE;
		}
		
		this.btn.setBackground(this.originalBGColour);
		
		this.btn.addActionListener(event -> {
			Main.getCurrentPlayer().setSelection(this);
		});
	}

	public Piece getPiece() {return this.piece;}
	public JButton getBtn() {return this.btn;}
	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public Color getOriginalBGColour() {return this.originalBGColour;}
	public Color getCurrentBGColour() {return this.currentBGColour;}
	
	public void setCurrentBGColour(Color colour) {
		this.currentBGColour = colour;
	}
	
	public void removeCoveredSquares(Piece piece) {
		
	}
	
	public void setPiece(Piece piece) {
		this.piece = piece;
		if (this.piece != null) {
			this.btn.setIcon(new ImageIcon(piece.getImage()));
		} else {
			this.btn.setIcon(null);
		}
	}
		
}