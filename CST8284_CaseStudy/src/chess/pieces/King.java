package chess.pieces;

import java.util.ArrayDeque;

import chess.Main;
import chess.Player;
import chess.Square;
import gui.Board;

public final class King extends Piece {
	private boolean castlingMove = false;
	private Square castlingRook;
	
	public King(Player owner) {
		super(owner, 1, "K");
	}
	
	public boolean castlePossible() {return this.castlingMove;}
	public Square getCastlingRook() {return this.castlingRook;}
	
	public void notCastlingMove() {
		this.castlingMove = false;
	}
	
	private void setCastlingMove(ArrayDeque<Square> arr, Square sq, Square rookPiece) {
		this.castlingMove = true;
		arr.add(sq);
		this.castlingRook = rookPiece;
	}
	
	public ArrayDeque<Square> findMoves(Square current) {
		ArrayDeque<Square> possibleMoves = new ArrayDeque<Square>();
		int x = current.getX();
		int y = current.getY();
		
		// Cardinal points
		possibleMoves.add(new Square(x-1,y));
		possibleMoves.add(new Square(x+1,y));
		possibleMoves.add(new Square(x,y+1));
		possibleMoves.add(new Square(x,y-1));
		// Diagonals		
		possibleMoves.add(new Square(x-1,y+1));
		possibleMoves.add(new Square(x-1,y-1));
		possibleMoves.add(new Square(x+1,y+1));
		possibleMoves.add(new Square(x+1,y-1));
		
		if (Main.boardReversed) {
			if (this.isFirstMove() && Board.boardArray[x][y+1].getPiece() == null && Board.boardArray[x][y+2].getPiece() == null && Board.boardArray[x][y+3].getPiece() instanceof Rook && Board.boardArray[x][y+3].getPiece().isFirstMove()) {setCastlingMove(possibleMoves, new Square(x,y+2), Board.boardArray[x][y+3]);}
			if (this.isFirstMove() && Board.boardArray[x][y-1].getPiece() == null && Board.boardArray[x][y-2].getPiece() == null && Board.boardArray[x][y-3].getPiece() == null && Board.boardArray[x][y-4].getPiece() instanceof Rook && Board.boardArray[x][y-4].getPiece().isFirstMove()) {setCastlingMove(possibleMoves, new Square(x,y-2), Board.boardArray[x][y-4]);}	
		} else {
			if (this.isFirstMove() && Board.boardArray[x][y-1].getPiece() == null && Board.boardArray[x][y-2].getPiece() == null && Board.boardArray[x][y-3].getPiece() instanceof Rook && Board.boardArray[x][y-3].getPiece().isFirstMove()) {setCastlingMove(possibleMoves, new Square(x,y-2), Board.boardArray[x][y-3]);}
			if (this.isFirstMove() && Board.boardArray[x][y+1].getPiece() == null && Board.boardArray[x][y+2].getPiece() == null && Board.boardArray[x][y+3].getPiece() == null && Board.boardArray[x][y+4].getPiece() instanceof Rook && Board.boardArray[x][y+4].getPiece().isFirstMove()) {setCastlingMove(possibleMoves, new Square(x,y+2), Board.boardArray[x][y+4]);}
		}
		
		
		validateMoves(possibleMoves, x, y);
		return this.validMoves;
	}
}
