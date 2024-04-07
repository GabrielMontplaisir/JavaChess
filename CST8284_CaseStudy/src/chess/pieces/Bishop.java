package chess.pieces;

import chess.Player;
import chess.Square;
import gui.Board;

public final class Bishop extends Piece {
	public Bishop(Player owner) {
		super(owner, 3, "B");
	}
	
	public void findMoves(Square current) {
		int x = current.getX();
		int y = current.getY();
		Square sq;
		
		for (int i = 1; i <= 7; i++) {
			if (x+i <=7 && y+i <= 7) {
				sq = Board.boardArray[x+i][y+i];
				this.southEastMoves.add(sq);
			}
			
			if (x+i <=7 && y-i >= 0) {
				sq = Board.boardArray[x+i][y-i];
				this.southWestMoves.add(sq);
			}
			if (x-i >=0 && y+i <= 7) {
				sq = Board.boardArray[x-i][y+i];
				this.northEastMoves.add(sq);
			}
			if (x-i >=0 && y-i >= 0) {
				sq = Board.boardArray[x-i][y-i];
				this.northWestMoves.add(sq);
			}
		}
		
		this.getPossibleMoves().add(this.southEastMoves);
		this.getPossibleMoves().add(this.southWestMoves);
		this.getPossibleMoves().add(this.northEastMoves);
		this.getPossibleMoves().add(this.northWestMoves);
		
		validateMoves(this.getPossibleMoves());
	}
}
