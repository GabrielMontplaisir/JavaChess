package chess.pieces;

import java.util.ArrayDeque;

import chess.Player;
import chess.Square;

public final class Knight extends Piece {
	public Knight(Player owner) {
		super(owner, 3, "N");
	}
	
	public ArrayDeque<Square> findMoves(Square current) {
		ArrayDeque<Square> possibleMoves = new ArrayDeque<Square>();
		int x = current.getX();
		int y = current.getY();
		
		// No need to worry about pieces in front as the knight can jump.
		possibleMoves.add(new Square(x-1,y-2));
		possibleMoves.add(new Square(x+1,y-2));
		possibleMoves.add(new Square(x+2,y-1));
		possibleMoves.add(new Square(x+2,y+1));
		possibleMoves.add(new Square(x+1,y+2));
		possibleMoves.add(new Square(x-1,y+2));
		possibleMoves.add(new Square(x-2,y-1));
		possibleMoves.add(new Square(x-2,y+1));
		
		validateMoves(possibleMoves);
		return this.validMoves;
	}
}
