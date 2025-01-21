package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
        IsInsideBoard insideBoard = new IsInsideBoard();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = chessPosition.getRow();
        int col = chessPosition.getColumn();

        int[][] possibleMoves = new int[][]{
                //UP Left/Right
                {row + 1, col - 2}, {row + 2, col - 1}, {row +2, col + 1}, {row + 1, col + 2},
                //DOWN Left/Right
                {row - 1, col - 2}, {row - 2, col - 1}, {row - 2, col +1}, {row - 1, col + 2}};
        for(int[] possibleMove: possibleMoves) {
            ChessPosition endPosition = new ChessPosition(possibleMove[0], possibleMove[1]);
            if(insideBoard.insideBoard(endPosition) && ((board.getPiece(endPosition) == null) || ( board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()))) {
                moves.add(new ChessMove(chessPosition, endPosition, null));
            }
        }
        return moves;
    }
}
