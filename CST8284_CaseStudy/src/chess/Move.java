package chess;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Queen;
import chess.pieces.Rook;
import gui.Board;
import gui.GameOver;

public class Move {
	private final String RANK = "abcdefgh";
	private final String RANK_REVERSED = "hgfedcba";
	private final Color HIGHLIGHT = new Color(255, 240, 190);
	
	private String special = "";
	private Square last, current;
	private Piece lastPiece, currentPiece;
	private String coord;
	private boolean promotionPieceSet = false;
	
// =================================== CONSTRUCTOR ===================================
	
	Move(final Square last, final Square current) {
		this.handleMove(last, current);
	}
	
	public Move(String notation) {
			this.parseNotation(notation);
	}
	
// =================================== GETTER METHODS ===================================
	
	public Piece getCurrentPiece() {return this.currentPiece;}
	public String getCoord() {return this.coord;}
	public Square getLast() {return this.last;}
	public Square getCurrent() {return this.current;}
	public Color getHIGHLIGHT() {return this.HIGHLIGHT;}
	public String getRank() {return this.RANK;}
	public String getRankReversed() {return this.RANK_REVERSED;}
	public String getSpecial() {return this.special;}
	public boolean getPromotionPieceSet() {return this.promotionPieceSet;}
	

// =================================== SETTER METHODS ===================================


	public void setRankAndFile(String special, String last) {
		if (special == null) special = "";
		if (last == null) last = "";
		this.coord = (Main.getBoardReversed()) ? special+RANK.charAt(this.current.getY())+(8-this.current.getX())+last : special+RANK.charAt(7-this.current.getY())+(this.current.getX()+1)+last;
	}
	
	public void setSpecial(String string) {
		this.special = string;
	}
	
	public void setCoordinate(String string) {
		this.coord = string;
	}
	
	
	public void setPromotionPieceSet() {
		this.promotionPieceSet = true;
	}

// =================================== FINISH MOVE METHOD ===================================
	
	private void handleMove(Square last, Square current) {
		this.last = last;
		this.current = current;
		this.lastPiece = this.last.getPiece();
		this.currentPiece = this.current.getPiece();
		
		// Handle if it's first move.		
		this.lastPiece.handleFirstMove();
		
		// If player is in check
		this.handleRemoveKingCheck();
		
		// If this is a pawn		
		if (this.lastPiece instanceof Pawn) {
			// Handle En Passant Moves
			boolean enPassant = ((Pawn) this.lastPiece).handleEnPassantMove(this);
			if (enPassant) {
				this.finishMove(this.lastPiece);
				return;
			}
			
			// Handle Promotion		
			boolean pawnPromotion = ((Pawn) this.lastPiece).handlePawnPromotion(this);
			if (pawnPromotion) {
				this.finishMove(((Pawn) this.lastPiece).getPromotionPiece());
				return;
			}
			
			// If capture with pawn -- Not en Passant and not promotion
			this.lastPiece.handleCapture(this);
			
			this.setRankAndFile(this.special, null);
			this.finishMove(this.lastPiece);
			return;
		}
		
		String disambiguation = this.handleDisambiguation();
		
		// If regular piece captures
		this.lastPiece.handleCapture(this);
		this.setRankAndFile(this.lastPiece.getName()+disambiguation+this.special, null);
		
		// If this is a king
		if (this.lastPiece instanceof King) {
			// If it's a left castling move			
			if (((King) this.lastPiece).leftCastlePossible() && this.last.getY()-this.current.getY() == 2) {
				((King) this.lastPiece).handleCastlingMove(this, ((King) this.lastPiece).getLeftCastlingRook(), this.getCurrent().getY()+1, Main.getBoardReversed());
				this.coord = (Main.getBoardReversed()) ? "O-O-O" : "O-O";
				
			// If it's a right castling move
			} else if (((King) this.lastPiece).rightCastlePossible() && this.last.getY()-this.current.getY() == -2) {
				((King) this.lastPiece).handleCastlingMove(this, ((King) this.lastPiece).getRightCastlingRook(), this.getCurrent().getY()-1, Main.getBoardReversed());
				this.coord = (Main.getBoardReversed()) ? "O-O" : "O-O-O";
			}
			((King) this.lastPiece).notCastlingMove();
		}
		
		this.finishMove(this.lastPiece);
	}

	public void finishMove(Piece piece) {
		
		// Move piece to new spot
		this.current.setPiece(piece);
		this.currentPiece = this.current.getPiece();
		
		// If this piece had a covered line and moved, then remove it.
		if (this.currentPiece.getOwner().getCoveredLines().containsKey(this.currentPiece)) this.currentPiece.getOwner().getCoveredLines().remove(this.currentPiece);
		// Remove the last square's piece.		
		this.last.setPiece(null);
		this.lastPiece = this.last.getPiece();
		
		// Update PlayerPieces square and refresh the player moves.
		this.currentPiece.getOwner().getPlayerPieces().replace(this.currentPiece, this.current);
		this.currentPiece.getOwner().refreshMoves();
		
		// Verify if this puts the opponent's king in check
		this.opponentKingChecked();
		
		// Update the opponent's valid moves
		this.currentPiece.getOwner().getOpponent().refreshMoves();
		
		// Verify if game is checkmate or stalemate
		boolean staleOrMate = this.verifyGameOutcome(Main.getGameOverPopUp());
		
		// Update Move Panel text
		Main.getMovePanel().updateLastMove(this);
		
		// Check if the game is a draw
		boolean draw = this.verifyDraw(Main.getGameOverPopUp());
		
		// If the game is over, then show the game pop up.
		if (staleOrMate || draw) Main.getGameOverPopUp().handleGameOver();
	}

	

// =================================== HANDLER METHODS ===================================
	
/*
 * VERIFY IF MOVE CHECKS OPPONENT KING
 * 
 * Find's the opponent's King, then finds the attacking piece's "line" containing the king.
 * Sets the player as checked, then adds the piece / line to a HashMap.
 * 
 * Update the checked King to have a red background.
 */
	
	private void opponentKingChecked() {
		if (this.currentPiece.getOwner().getOpponent().isKingChecked()) return;
		
		for (Piece p : this.currentPiece.getOwner().getPlayerPieces().keySet()) {
			
			// Find opponent's king
			Optional<Square> opponentKingSquare = p.getCoveringSquares().stream().filter(sq -> sq.getPiece() instanceof King && sq.getPiece().getOwner().isLightPieces() != p.getOwner().isLightPieces()).findFirst();
			if (opponentKingSquare.isPresent()) {
				
				// Find the appropriate "line" containing the king. Add the piece's current square.
				Optional<ArrayDeque<Square>> attackingLine = p.getPossibleMoves().stream().filter(line -> line.contains(opponentKingSquare.get())).findFirst();
				
				// Set Player as checked, and add the piece as an attacking piece along with the "line".
				opponentKingSquare.get().getPiece().getOwner().setChecked(true);
				if (p instanceof Knight) {
					attackingLine.get().clear();
					attackingLine.get().add(opponentKingSquare.get());
				}
				
				attackingLine.get().add(this.currentPiece.getOwner().getPlayerPieces().get(p));
				opponentKingSquare.get().getPiece().getOwner().getAttackingPieces().put(p, attackingLine.get());
				
				// Indicate King is checked. Set background to red.			
				opponentKingSquare.get().setCurrentBGColour(Color.red);
				opponentKingSquare.get().getBtn().setBackground(opponentKingSquare.get().getCurrentBGColour());
				if (!this.coord.endsWith("+")) this.coord += "+";
			}
		}
	}
		
/*
 * VERIFY IF OPPONENT PLAYER IS CHECKMATED
 * If the total collective moves for the opponent player is 0 (or less, which is impossible, but set for good measure), then the player is checkmated.
 */
	
	private boolean verifyGameOutcome(GameOver pane) {
		
		// If player can no longer move, then it's a checkmate or stalemate, depending on situation...
		if (this.currentPiece.getOwner().getOpponent().getCollectiveMoves().size() <= 0) {
			
			// If game is stalemate
			if (!this.currentPiece.getOwner().getOpponent().isKingChecked()) {
				this.coord += "1/2-1/2";
				pane.setMessage("Draw by Stalemate");
				pane.setIcon(null);
				return true;
			}
			
			// If game is checkmate
			if (this.currentPiece.getOwner().getOpponent().isKingChecked()) {
				String colour = (this.currentPiece.getOwner().isLightPieces()) ? "light": "dark";
				ImageIcon image = null;
				
				try {
					image = new ImageIcon(ImageIO.read(this.getClass().getResource("/"+colour+"_k.png")));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				this.coord = this.coord.replace("+", "#");
				pane.setMessage("Checkmate! "+this.currentPiece.getOwner().getPlayerBox().getPlayerTitle().getText()+" wins!");
				pane.setIcon(image);
				return true;
			}
		}
		
		return false;
		
	}
	
/*
 * VERIFY IF THERE IS A DRAW
 * If the two players only have Kings left, or if the same player repeats the same moves three times, then it's a draw.
 */
		
	private boolean verifyDraw(GameOver pane) {
		// If current player or opponent ONLY have kings left, then it's a draw.
		if (this.currentPiece.getOwner().getPlayerPieces().size() == 1 && this.currentPiece.getOwner().getOpponent().getPlayerPieces().size() == 1) {
			this.coord += "1/2-1/2";
			pane.setMessage("Draw by lack of pieces.");
			pane.setIcon(null);
			return true;
		}
		
		// If same moves 3 times, then draw.
		if (Main.getMovePanel().getMoves().size() > 9) {
			HashSet<String> temp = new HashSet<String>();
			
			int counter = 0;
			
			for (String el : Main.getMovePanel().getMoves().reversed()) {
				if (counter >= 9) break;
				
				temp.add(el);
				counter++;
			}
			
			if (temp.size() < 5) {
				this.coord += "1/2-1/2";
				pane.setMessage("Draw by Repetition.");
				pane.setIcon(null);
				return true;
			}
		}
		
		return false;
	}
	
/*
 * HANDLE IF WE NEED TO REMOVE THE CHECK FROM PLAYER KING
 * Will remove check if a piece moves in front of King or blocked check for a king.
 * Then, remove checked square highlight for the King.
 * 
 * If the player moved the king, then we'll remove the attacker's line and remove the highlight for the king.
 */

	private void handleRemoveKingCheck() {
		if (!this.lastPiece.getOwner().isKingChecked()) return;
		
		this.lastPiece.getOwner().setChecked(false);
		this.lastPiece.getOwner().getAttackingPieces().clear();
		if (!(this.lastPiece instanceof King)) {
			Square kingSquare = null;
			for (Piece p : this.lastPiece.getOwner().getPlayerPieces().keySet()) {
				if (p instanceof King) {
					kingSquare = this.lastPiece.getOwner().getPlayerPieces().get(p);
					break;
				}
			}
			kingSquare.setCurrentBGColour(kingSquare.getOriginalBGColour());
			kingSquare.getBtn().setBackground(kingSquare.getCurrentBGColour());	
		} else {
			this.lastPiece.getOwner().getOpponent().getCoveredLines().clear();
			this.last.setCurrentBGColour(this.last.getOriginalBGColour());
			this.last.getBtn().setBackground(this.last.getCurrentBGColour());	
		}
	}
	
/*
 * Handle disambiguation in pieces of the same colour
 * If two pieces can move to the same square of the same type, then add the rank in front of the move.
 * 
 */

	private String handleDisambiguation() {
		for (Piece p : this.lastPiece.getOwner().getPlayerPieces().keySet()) {
			if (p.getClass().equals(this.lastPiece.getClass()) && p != this.lastPiece) {
				if (p.getValidSquares().contains(this.current)) {
					if (Main.getBoardReversed()) {
						return String.valueOf(RANK.charAt(this.last.getY()));
					} else {
						return String.valueOf(RANK.charAt(7-this.last.getY()));
					}
					
				}
			}
		}
		return "";
		
	}
	
// =================================== PARSING METHODS ===================================
	
	private void parseNotation(String coord) {
		int or, rca, fca, dis = -1, fileIndex, rankIndex, yMatch;
		// If it's a pawn
		if (coord.matches("^[a-h](x[a-h])?[1-8](=[QRBN])?[+#]?$")) {
			if (coord.charAt(1) == 'x') {
				or = (Main.getBoardReversed()) ? this.getRank().indexOf(coord.charAt(0)) : this.getRankReversed().indexOf(coord.charAt(0));
				rca = (Main.getBoardReversed()) ? this.getRank().indexOf(coord.charAt(2)) : this.getRankReversed().indexOf(coord.charAt(2));
				fca = Integer.parseInt(String.valueOf(coord.charAt(3)));
				yMatch = or;
			} else {
				rca = (Main.getBoardReversed()) ? this.getRank().indexOf(coord.charAt(0)) : this.getRankReversed().indexOf(coord.charAt(0));
				fca = Integer.parseInt(String.valueOf(coord.charAt(1)));
				yMatch = rca;
			}
			
			rankIndex = rca;
			fileIndex = (Main.getBoardReversed()) ? 8 - fca : fca - 1;
			
			for (Piece p : Main.getCurrentPlayer().getPlayerPieces().keySet()) {
				if (p instanceof Pawn && Main.getCurrentPlayer().getPlayerPieces().get(p).getY() == yMatch) {
					if (p.getValidSquares().contains(Board.getSquare(fileIndex, rankIndex))) {
						if (coord.indexOf('=') != -1) {
							int equalSignPos = coord.indexOf('=');
							switch(coord.charAt(equalSignPos+1)) {
							case 'Q':
								((Pawn) p).setPromotionPiece(new Queen(p.getOwner()));
								break;
							case 'B':
								((Pawn) p).setPromotionPiece(new Bishop(p.getOwner()));
								break;
							case 'N':
								((Pawn) p).setPromotionPiece(new Knight(p.getOwner()));
								break;
							case 'R':
								((Pawn) p).setPromotionPiece(new Rook(p.getOwner()));
								break;
							}

							this.setPromotionPieceSet();
						}
						this.handleMove(Main.getCurrentPlayer().getPlayerPieces().get(p), Board.getSquare(fileIndex, rankIndex));
						break;
					}
				}
			}
		}
		
		if (coord.matches("^[QKBNR][a-h]?[x]?[a-h][1-8][+#]?$")) {
			boolean moveHandled = false;
			
			if ((coord.indexOf('x') == -1 && Character.isLetter(coord.charAt(1)) && Character.isLetter(coord.charAt(2))) || (coord.indexOf('x') != -1 && coord.charAt(1) != 'x')) {
				dis = (Main.getBoardReversed()) ? this.getRank().indexOf(coord.charAt(1)) : this.getRankReversed().indexOf(coord.charAt(1));
			} 
			
			if (coord.indexOf('x') != -1) {
				int xIn = coord.indexOf('x');
				rca = (Main.getBoardReversed()) ? this.getRank().indexOf(coord.charAt(xIn + 1)) : this.getRankReversed().indexOf(coord.charAt(xIn + 1));
				fca = Integer.parseInt(String.valueOf(coord.charAt(xIn + 2)));
			} else if (dis != -1)  {
				rca = (Main.getBoardReversed()) ? this.getRank().indexOf(coord.charAt(2)) : this.getRankReversed().indexOf(coord.charAt(2));
				fca = Integer.parseInt(String.valueOf(coord.charAt(3)));
			} else {
				rca = (Main.getBoardReversed()) ? this.getRank().indexOf(coord.charAt(1)) : this.getRankReversed().indexOf(coord.charAt(1));
				fca = Integer.parseInt(String.valueOf(coord.charAt(2)));
			}
			
			fileIndex = (Main.getBoardReversed()) ? 8 - fca : fca - 1;
			rankIndex = rca;
			for (Piece p : Main.getCurrentPlayer().getPlayerPieces().keySet()) {
				if (dis != -1 && Main.getCurrentPlayer().getPlayerPieces().get(p).getY() != dis) {
					continue;
				};
						
				switch (coord.charAt(0)) {
					case 'Q':
						if (p instanceof Queen && p.getValidSquares().contains(Board.getSquare(fileIndex, rankIndex))) {
							this.handleMove(Main.getCurrentPlayer().getPlayerPieces().get(p), Board.getSquare(fileIndex, rankIndex));
							moveHandled = true;
						}
						break;
					case 'K':
						if (p instanceof King && p.getValidSquares().contains(Board.getSquare(fileIndex, rankIndex))) {
							this.handleMove(Main.getCurrentPlayer().getPlayerPieces().get(p), Board.getSquare(fileIndex, rankIndex));
							moveHandled = true;
						}
						break;
					case 'B':
						if (p instanceof Bishop && p.getValidSquares().contains(Board.getSquare(fileIndex, rankIndex))) {
							this.handleMove(Main.getCurrentPlayer().getPlayerPieces().get(p), Board.getSquare(fileIndex, rankIndex));
							moveHandled = true;
						}
						break;
					case 'N':
						if (p instanceof Knight && p.getValidSquares().contains(Board.getSquare(fileIndex, rankIndex))) {
							this.handleMove(Main.getCurrentPlayer().getPlayerPieces().get(p), Board.getSquare(fileIndex, rankIndex));
							moveHandled = true;
						}
						break;
					case 'R':
						if (p instanceof Rook && p.getValidSquares().contains(Board.getSquare(fileIndex, rankIndex))) {
							this.handleMove(Main.getCurrentPlayer().getPlayerPieces().get(p), Board.getSquare(fileIndex, rankIndex));
							moveHandled = true;
						}
						break;
				}
			if (moveHandled) break;
			}
		}
		
		if (coord.matches("^O-O(-O)?[+#]?$")) {
			for (Piece p : Main.getCurrentPlayer().getPlayerPieces().keySet()) {
				if (p instanceof King) {
					Square kingSquare = Main.getCurrentPlayer().getPlayerPieces().get(p);
					int yDir;
					if (Main.getBoardReversed()) { 
						yDir = (coord.matches("^O-O[+#]?$")) ? kingSquare.getY() + 2 : kingSquare.getY() - 2;
					} else {
						yDir = (coord.matches("^O-O[+#]?$")) ? kingSquare.getY() - 2 : kingSquare.getY() + 2;
					}
					
					if (p.getValidSquares().contains(Board.getSquare(kingSquare.getX(), yDir))) this.handleMove(kingSquare, Board.getSquare(kingSquare.getX(), yDir));
					
				}
			}
		}
	}
}