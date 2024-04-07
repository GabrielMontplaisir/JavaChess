package chess.pieces;

import chess.Player;
import chess.Square;
import gui.Board;

public final class Queen extends Piece {
	public Queen(Player owner) {
		super(owner, 9, "Q");
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
		
		for (int i = x+1; i <= 7; i++) {
			this.southMoves.add(Board.boardArray[i][y]);
		}
		for (int i = x-1; i >= 0; i--) {
			this.northMoves.add(Board.boardArray[i][y]);
		}
		for (int i = y+1; i <= 7; i++) {
			this.eastMoves.add(Board.boardArray[x][i]);
		}
		for (int i = y-1; i >= 0; i--) {
			this.westMoves.add(Board.boardArray[x][i]);
		}
		
		this.getPossibleMoves().add(this.southWestMoves);
		this.getPossibleMoves().add(this.southEastMoves);
		this.getPossibleMoves().add(this.northEastMoves);
		this.getPossibleMoves().add(this.northWestMoves);
		this.getPossibleMoves().add(this.eastMoves);
		this.getPossibleMoves().add(this.westMoves);
		this.getPossibleMoves().add(this.northMoves);
		this.getPossibleMoves().add(this.southMoves);
		
		validateMoves(this.getPossibleMoves());
	}
}
