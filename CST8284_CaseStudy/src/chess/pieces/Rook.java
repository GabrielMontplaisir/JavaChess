package chess.pieces;

import java.util.ArrayDeque;

import chess.Player;
import chess.Square;
import gui.Board;

public final class Rook extends Piece {
	
	public Rook(Player owner) {
		super(owner, 5, "R");
	}
	
	public ArrayDeque<Square> findMoves(Square current) {
		ArrayDeque<Square> possibleMoves = new ArrayDeque<Square>();
		int x = current.getX();
		int y = current.getY();
		
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
		
		validateMoves(possibleMoves, x, y);
		return this.validMoves;
	}
}
