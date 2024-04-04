package chess.pieces;

import java.util.ArrayDeque;

import chess.Main;
import chess.Player;
import chess.Square;
import gui.Board;

public final class King extends Piece {
	private boolean leftCastlingMove = false;
	private Square leftCastlingRook;
	private boolean rightCastlingMove = false;
	private Square rightCastlingRook;
	
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
			if (this.isFirstMove() && Board.boardArray[x][y+1].getPiece() == null && Board.boardArray[x][y+2].getPiece() == null && Board.boardArray[x][y+3].getPiece() instanceof Rook && Board.boardArray[x][y+3].getPiece().isFirstMove()) {setRightCastlingMove(possibleMoves, new Square(x,y+2), Board.boardArray[x][y+3]);}
			if (this.isFirstMove() && Board.boardArray[x][y-1].getPiece() == null && Board.boardArray[x][y-2].getPiece() == null && Board.boardArray[x][y-3].getPiece() == null && Board.boardArray[x][y-4].getPiece() instanceof Rook && Board.boardArray[x][y-4].getPiece().isFirstMove()) {setLeftCastlingMove(possibleMoves, new Square(x,y-2), Board.boardArray[x][y-4]);}	
		} else {
			if (this.isFirstMove() && Board.boardArray[x][y-1].getPiece() == null && Board.boardArray[x][y-2].getPiece() == null && Board.boardArray[x][y-3].getPiece() instanceof Rook && Board.boardArray[x][y-3].getPiece().isFirstMove()) {setLeftCastlingMove(possibleMoves, new Square(x,y-2), Board.boardArray[x][y-3]);}
			if (this.isFirstMove() && Board.boardArray[x][y+1].getPiece() == null && Board.boardArray[x][y+2].getPiece() == null && Board.boardArray[x][y+3].getPiece() == null && Board.boardArray[x][y+4].getPiece() instanceof Rook && Board.boardArray[x][y+4].getPiece().isFirstMove()) {setRightCastlingMove(possibleMoves, new Square(x,y+2), Board.boardArray[x][y+4]);}
		}
		
		
		validateMoves(possibleMoves);
		return this.validMoves;
	}
}
