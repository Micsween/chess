package chess.movement;


import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    ValidPosition validPosition = new ValidPosition();

    public KnightMovesCalculator() {
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = chessPosition.getRow();
        int col = chessPosition.getColumn();

        int[][] possiblePositions = {
                {row + 1, col - 2},
                {row + 2, col - 1},
                {row + 1, col + 2},
                {row + 2, col + 1},
                {row - 1, col - 2},
                {row - 2, col - 1},
                {row - 1, col + 2},
                {row - 2, col + 1},
        };
        moves = validPosition.createPositions(possiblePositions, board, moves, piece, chessPosition);
        return moves;
    }
}
