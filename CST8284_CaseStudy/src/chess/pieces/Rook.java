package chess.pieces;

import chess.Player;
import chess.Square;
import gui.Board;

public final class Rook extends Piece {
	
	public Rook(Player owner) {
		super(owner, 5, "R");
	}
	
	public void findMoves(Square current) {
		int x = current.getX();
		int y = current.getY();
		
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
		
		
		this.getPossibleMoves().add(this.eastMoves);
		this.getPossibleMoves().add(this.westMoves);
		this.getPossibleMoves().add(this.northMoves);
		this.getPossibleMoves().add(this.southMoves);
		
		validateMoves(this.getPossibleMoves());
	}
}
