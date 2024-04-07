package chess.pieces;

import java.util.ArrayDeque;

import chess.Main;
import chess.Player;
import chess.Square;
import gui.Board;

public final class King extends Piece {
	private boolean leftCastlingMove = false;
	private Square leftCastlingRook, rightCastlingRook;
	private boolean rightCastlingMove = false;
	
	public King(Player owner) {
		super(owner, 1, "K");
	}
	
	public boolean leftCastlePossible() {return this.leftCastlingMove;}
	public Square getLeftCastlingRook() {return this.leftCastlingRook;}
	public boolean rightCastlePossible() {return this.rightCastlingMove;}
	public Square getRightCastlingRook() {return this.rightCastlingRook;}
	
	public void notCastlingMove() {
		this.leftCastlingMove = false;
		this.rightCastlingMove = false;
	}
	
	private void setLeftCastlingMove(ArrayDeque<Square> arr, Square sq, Square rookPiece) {
		this.leftCastlingMove = true;
		arr.add(sq);
		this.leftCastlingRook = rookPiece;
	}
	
	private void setRightCastlingMove(ArrayDeque<Square> arr, Square sq, Square rookPiece) {
		this.rightCastlingMove = true;
		arr.add(sq);
		this.rightCastlingRook = rookPiece;
	}
	
	private void checkLeftCastle(boolean reversed, int x, int y) {
		if (!this.isFirstMove() || Board.boardArray[x][y-1].getPiece() != null || Board.boardArray[x][y-2].getPiece() != null || this.getOwner().isKingChecked()) return;
		for (Piece p : this.getOwner().getOpponent().getPlayerPieces().keySet()) {
			if (p.getCoveringSquares().contains(Board.boardArray[x][y-1]) || p.getCoveringSquares().contains(Board.boardArray[x][y-2])) {
				return;}
			if (reversed && p.getCoveringSquares().contains(Board.boardArray[x][y-3])) return;	
		}
		
		if (reversed) {
			if (Board.boardArray[x][y-3].getPiece() == null && Board.boardArray[x][y-4].getPiece() instanceof Rook && Board.boardArray[x][y-4].getPiece().isFirstMove()) {setLeftCastlingMove(this.westMoves, Board.boardArray[x][y-2], Board.boardArray[x][y-4]);}	
		} else {
			if (Board.boardArray[x][y-3].getPiece() instanceof Rook && Board.boardArray[x][y-3].getPiece().isFirstMove()) {setLeftCastlingMove(this.westMoves, Board.boardArray[x][y-2], Board.boardArray[x][y-3]);}
		}
	}
	
	private void checkRightCastle(boolean reversed, int x, int y) {
		if (!this.isFirstMove() || Board.boardArray[x][y+1].getPiece() != null || Board.boardArray[x][y+2].getPiece() != null || this.getOwner().isKingChecked()) return;
		
		for (Piece p : this.getOwner().getOpponent().getPlayerPieces().keySet()) {
			if (p.getCoveringSquares().contains(Board.boardArray[x][y+1]) || p.getCoveringSquares().contains(Board.boardArray[x][y+2])) return;
			if (!reversed && p.getCoveringSquares().contains(Board.boardArray[x][y+3])) return;	
		}
		
		if (reversed) {
			if (Board.boardArray[x][y+3].getPiece() instanceof Rook && Board.boardArray[x][y+3].getPiece().isFirstMove()) {setRightCastlingMove(this.eastMoves, Board.boardArray[x][y+2], Board.boardArray[x][y+3]);}
		} else {
			if (Board.boardArray[x][y+3].getPiece() == null && Board.boardArray[x][y+4].getPiece() instanceof Rook && Board.boardArray[x][y+4].getPiece().isFirstMove()) {setRightCastlingMove(this.eastMoves, Board.boardArray[x][y+2], Board.boardArray[x][y+4]);}
		}
	}
	
	public void findMoves(Square current) {
		int x = current.getX();
		int y = current.getY();
		
		if (x-1 >= 0) {
			this.northMoves.add(Board.boardArray[x-1][y]);
			if (y+1 <= 7) this.northEastMoves.add(Board.boardArray[x-1][y+1]);
			if (y-1 >= 0) this.northWestMoves.add(Board.boardArray[x-1][y-1]);
		}
		
		if (x+1 <= 7) {
			this.southMoves.add(Board.boardArray[x+1][y]);
			if (y+1 <= 7) this.southEastMoves.add(Board.boardArray[x+1][y+1]);
			if (y-1 >= 0) this.southWestMoves.add(Board.boardArray[x+1][y-1]);
		}
		
		if (y+1 <= 7) this.eastMoves.add(Board.boardArray[x][y+1]);
		if (y-1 >= 0) this.westMoves.add(Board.boardArray[x][y-1]);
		
		this.checkLeftCastle(Main.boardReversed, x, y);
		this.checkRightCastle(Main.boardReversed, x, y);
		
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
