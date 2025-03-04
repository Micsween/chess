package chess.movement;


import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    ValidPosition validPosition = new ValidPosition();

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

        for (int[] possiblePosition : possiblePositions) {
            ChessPosition endPosition = new ChessPosition(possiblePosition[0], possiblePosition[1]);
            if (validPosition.isValidPosition(board, endPosition, piece)) {
                moves.add(new ChessMove(chessPosition, endPosition, null));
            }
        }

        return moves;
    }
}