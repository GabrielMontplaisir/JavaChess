package chess.pieces;

import java.io.IOException;
import java.util.ArrayDeque;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import chess.Main;
import chess.Player;
import chess.Square;
import gui.Board;

public final class Pawn extends Piece {
	Piece promotionPiece;
	Object[] promotionOptions = new Object[4];
	JButton qBtn = new JButton();
	JButton rBtn = new JButton();
	JButton nBtn = new JButton();
	JButton bBtn = new JButton();
	
	
	private boolean moveTopDown;
	private Square leftDiag, rightDiag;
	private boolean secondMove = false;	
	private Square enPassantPiece;
	
	public Pawn(Player owner, boolean topDown) {
		super(owner, 1, "");
		this.moveTopDown = topDown;
		
		try {
			if (this.getOwner().isLightPieces() ) {
				this.qBtn.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/light_q.png"))));
				this.rBtn.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/light_r.png"))));
				this.nBtn.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/light_n.png"))));
				this.bBtn.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/light_b.png"))));
			} else {
				this.qBtn.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/dark_q.png"))));
				this.rBtn.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/dark_r.png"))));
				this.nBtn.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/dark_n.png"))));
				this.bBtn.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/dark_b.png"))));
			}
		} catch (IOException e) {
			System.out.println(e);
		}
		
		this.promotionOptions[0] = qBtn;
		this.promotionOptions[1] = rBtn;
		this.promotionOptions[2] = nBtn;
		this.promotionOptions[3] = bBtn;
		
		this.qBtn.addActionListener(e -> {
			this.promotionPiece = new Queen(this.getOwner());
			JOptionPane.getRootFrame().dispose();		
		});
		this.rBtn.addActionListener(e -> {
			this.promotionPiece = new Rook(this.getOwner());
			JOptionPane.getRootFrame().dispose();
			});
		this.nBtn.addActionListener(e -> {
			this.promotionPiece = new Knight(this.getOwner());
			JOptionPane.getRootFrame().dispose();
			});
		this.bBtn.addActionListener(e -> {
			this.promotionPiece = new Bishop(this.getOwner());
			JOptionPane.getRootFrame().dispose();
			});
	}
	
	public boolean isSecondMove() {return this.secondMove;}
	public boolean getTopDown() {return this.moveTopDown;}
	public Square getLeftDiag() {return this.leftDiag;}
	public Square getRightDiag() {return this.rightDiag;}
	public Piece getPromotionPiece() {return this.promotionPiece;}
	public Object[] getPromotionOptions() {return this.promotionOptions;}
	public Square getEnPassant() {return this.enPassantPiece;}
	
	public void setEnPassant(Square sq) {
		this.enPassantPiece = sq;
	}
	
	public void setSecondMove(boolean isSecond) {
		this.secondMove = isSecond;
	}
	
	// Reverse direction of piece movement
	
	private int moveDirection(int num) {
		if (moveTopDown) {return num * 1;}
		return num * -1;
	}
	
	// Used to check En Passant moves.
	private void addEnPassantMove(ArrayDeque<Square> array, Square item, int x, int y) {
		if (item.getPiece() != null && item.getPiece() instanceof Pawn && item.getPiece().getOwner().isLightPieces() != this.getOwner().isLightPieces() && Main.getLastMove().getCurrent().getPiece() == item.getPiece() && ((Pawn) item.getPiece()).isSecondMove()) {
			array.add(Board.boardArray[x][y]);
			setEnPassant(item);
		}
	}

	public void findMoves(Square current) {
		ArrayDeque<Square> tempMoves = new ArrayDeque<Square>();
		int x = current.getX();
		int y = current.getY();
		int possibleNegY = y-1;
		int possiblePosY = y+1;
		int possibleX = x+moveDirection(1);
		
		// Default possible moves
		if (possibleX >= 0 && possibleX <= 7) {
			if (Board.boardArray[possibleX][y].getPiece() == null) {tempMoves.add(Board.boardArray[possibleX][y]);}
			if (this.isFirstMove() && Board.boardArray[possibleX][y].getPiece() == null && Board.boardArray[x+moveDirection(2)][y].getPiece() == null) {tempMoves.add(Board.boardArray[x+moveDirection(2)][y]);}

			
			// Check diagonals			
			if (possibleNegY >= 0) {
				this.leftDiag = Board.boardArray[possibleX][possibleNegY];
				this.getCoveringSquares().add(this.leftDiag);
				if (this.leftDiag.getPiece() != null) {tempMoves.add(this.leftDiag);}
			}
			if (possiblePosY <= 7) {
				this.rightDiag = Board.boardArray[possibleX][possiblePosY];
				this.getCoveringSquares().add(this.rightDiag);
				if (this.rightDiag.getPiece() != null) {tempMoves.add(this.rightDiag);}
			}
		}
		
		// Check en passant
		if ((!this.getTopDown() && x == 3) || (this.getTopDown() && x == 4)) {
			if (possibleNegY >= 0 && possibleNegY <= 7) addEnPassantMove(tempMoves, Board.boardArray[x][possibleNegY], possibleX, possibleNegY);
			if (possiblePosY >= 0 && possiblePosY <= 7) addEnPassantMove(tempMoves, Board.boardArray[x][possiblePosY], possibleX, possiblePosY);
		} 
		
		this.getPossibleMoves().add(tempMoves);
		
		validateMoves(this.getPossibleMoves());
	}
}
