package chess.pieces;

import java.util.ArrayDeque;

import chess.Player;
import chess.Square;
import gui.Board;

public final class Bishop extends Piece {
	public Bishop(Player owner) {
		super(owner, 3, "B");
	}
	
	public ArrayDeque<Square> findMoves(Square current) {
		ArrayDeque<Square> possibleMoves = new ArrayDeque<Square>();
		int x = current.getX();
		int y = current.getY();
		boolean diag1 = false;
		boolean diag2 = false;
		boolean diag3 = false;
		boolean diag4 = false;
		
		for (int i = 1; i <= 7; i++) {
				if (!diag1 && x+i <=7 && y+i <= 7) {
					if (Board.boardArray[x+i][y+i].getPiece() != null) diag1 = true;
					possibleMoves.add(new Square(x+i,y+i));
				}
				if (!diag2  && x+i <=7 && y-i >= 0) {
					if (Board.boardArray[x+i][y-i].getPiece() != null) diag2 = true;
					possibleMoves.add(new Square(x+i,y-i));
				}
				if (!diag3 && x-i >=0 && y+i <= 7) {
					if (Board.boardArray[x-i][y+i].getPiece() != null) diag3 = true;
					possibleMoves.add(new Square(x-i,y+i));
				}
				if (!diag4 && x-i >=0 && y-i >= 0) {
					if (Board.boardArray[x-i][y-i].getPiece() != null) diag4 = true;
					possibleMoves.add(new Square(x-i,y-i));
				}
		}
		
		validateMoves(possibleMoves, x, y);
		return this.validMoves;
	}
}
