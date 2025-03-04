package chess.movement; //this is the directory that my file is in

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    ValidPosition validPosition = new ValidPosition();

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = chessPosition.getRow();
        int col = chessPosition.getColumn();

        int[][] possiblePositions = {
                {row + 1, col}, {row + 1, col - 1}, {row + 1, col + 1},
                {row, col - 1}, {row, col + 1}, {row - 1, col},
                {row - 1, col - 1}, {row - 1, col + 1}
        };
        //for each move in the first row of possible moves,
        //check if its valid and if it is add it to the Collection<ChessMove>
        for (int[] possiblePosition : possiblePositions) {
            ChessPosition endPosition = new ChessPosition(possiblePosition[0], possiblePosition[1]);
            if (validPosition.isValidPosition(board, endPosition, piece)) {
                moves.add(new ChessMove(chessPosition, endPosition, null));
            }
        }

        return moves;
    }
}