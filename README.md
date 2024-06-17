# Java Chess
By Gabriel Montplaisir

This is the final assignment for my a course at Algonquin College. It incorporates a full GUI using Java Swing, as well as the following features:

- An option to start a "New Game"
- Saving a game as .txt file using PGN format. You can then load this into any online chess PGN viewer.
- Loading a game from said .txt file. I've built the parser to recreate the moves.
- Highlighting possible & valid moves, and highlighting the last move.
- A move panel displaying what moves were played previously.
- Two player boxes to show which pieces have been captured.

To play, you click on a piece, and click on a teal square to move the piece.
The goal is to checkmate the opponent's King by not allowing it to move to other squares.

The game also implements the following:

- Checks for checks & checkmates
- Checks for stalemates
- Checks for 3-turn repetition (resulting in a draw).
