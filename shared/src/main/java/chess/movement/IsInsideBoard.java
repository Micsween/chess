package chess.movement;

import chess.ChessPosition;

public class IsInsideBoard {
    /**
     *
     * Returns true if a piece is inside of the board.
     */
    IsInsideBoard(){}
    boolean insideBoard(ChessPosition position){
        int row = position.getRow();
        int col = position.getColumn();
        return (row < 9 && row > 0 && col < 9 && col > 0);
    }
}
