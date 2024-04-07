package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import chess.Main;
import chess.Player;
import chess.Square;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class Board {
	public final static Square[][] boardArray = new Square[8][8];
	final JPanel boardGrid = new JPanel(new GridLayout(8, 8));
	
	public Board(final JPanel panel, final Player p1, final Player p2) {
		
		for (int i = 0; i < boardArray.length; i++) {
			for (int j = 0; j < boardArray[i].length; j++) {
				boardArray[i][j] = new Square(i, j);
				boardGrid.add(boardArray[i][j].getBtn());
			}
		}
		
		for (int i = 0; i < boardArray.length; i++) {
			boardArray[1][i].setPiece(new Pawn(p2, true));
			boardArray[6][i].setPiece(new Pawn(p1, false));
		}
		
		// P2 Pieces
		boardArray[0][0].setPiece(new Rook(p2));
		boardArray[0][1].setPiece(new Knight(p2));
		boardArray[0][2].setPiece(new Bishop(p2));
		boardArray[0][5].setPiece(new Bishop(p2));
		boardArray[0][6].setPiece(new Knight(p2));
		boardArray[0][7].setPiece(new Rook(p2));
		
		// P1 Pieces		
		boardArray[7][0].setPiece(new Rook(p1));
		boardArray[7][1].setPiece(new Knight(p1));
		boardArray[7][2].setPiece(new Bishop(p1));
		boardArray[7][5].setPiece(new Bishop(p1));
		boardArray[7][6].setPiece(new Knight(p1));
		boardArray[7][7].setPiece(new Rook(p1));
		
		// If dark pieces are on bottom		
		if (Main.boardReversed) {
			boardArray[0][4].setPiece(new King(p2));	
			boardArray[0][3].setPiece(new Queen(p2));
			boardArray[7][4].setPiece(new King(p1));
			boardArray[7][3].setPiece(new Queen(p1));
		} else {
			boardArray[0][3].setPiece(new King(p2));
			boardArray[0][4].setPiece(new Queen(p2));
			boardArray[7][3].setPiece(new King(p1));
			boardArray[7][4].setPiece(new Queen(p1));
		}
		
		for (int i = 0; i < boardArray.length; i++) {
			p1.getPlayerPieces().put(boardArray[6][i].getPiece(), boardArray[6][i]); // Add pawns
			p1.getPlayerPieces().put(boardArray[7][i].getPiece(), boardArray[7][i]); // Add pieces
			p2.getPlayerPieces().put(boardArray[1][i].getPiece(), boardArray[1][i]); // Add pawns
			p2.getPlayerPieces().put(boardArray[0][i].getPiece(), boardArray[0][i]); // Add pieces
		}
		
		p1.refreshMoves();
		p2.refreshMoves();
		
		boardGrid.setBorder(new EmptyBorder(15, 0, 5, 0));
		panel.add(boardGrid, BorderLayout.LINE_START);
	}
}
