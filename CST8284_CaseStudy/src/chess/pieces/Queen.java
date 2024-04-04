package chess.pieces;

import java.util.ArrayDeque;

import chess.Player;
import chess.Square;
import gui.Board;

public final class Queen extends Piece {
	public Queen(Player owner) {
		super(owner, 9, "Q");
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
		
		for (int i = x+1; i <= 7; i++) {
			possibleMoves.add(new Square(i,y));
			if (Board.boardArray[i][y].getPiece() != null) break;
		}
		for (int i = x-1; i >= 0; i--) {
			possibleMoves.add(new Square(i,y));
			if (Board.boardArray[i][y].getPiece() != null) break;
		}
		for (int i = y+1; i <= 7; i++) {
			possibleMoves.add(new Square(x,i));
			if (Board.boardArray[x][i].getPiece() != null) break;
		}
		for (int i = y-1; i >= 0; i--) {
			possibleMoves.add(new Square(x,i));
			if (Board.boardArray[x][i].getPiece() != null) break;
		}
		
		validateMoves(possibleMoves);
		return this.validMoves;
	}
}
