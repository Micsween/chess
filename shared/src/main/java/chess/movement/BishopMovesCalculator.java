package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator{
    IsInsideBoard isInsideBoard = new IsInsideBoard();
    //can move diagonal right up {row + 1, col +1}
    //can move diagonal left up {row + 1, col - 1}
    //can move diagonal right down {row - 1, col + 1}
    //can move diagonal left down {row -1, col - 1}
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        Collection<ChessMove> moves = moveDiagonal(board, chessPosition, piece , 1, 1);
        moves.addAll(moveDiagonal(board, chessPosition, piece , 1, -1));
        moves.addAll(moveDiagonal(board, chessPosition, piece , -1, 1));
        moves.addAll(moveDiagonal(board, chessPosition, piece , -1, -1));
        return moves;
    }
    public Collection<ChessMove> moveDiagonal(ChessBoard board, ChessPosition chessPosition, ChessPiece piece, int rowModifier, int colModifier) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        //create a new end position, check if its valid, and then add it to the collection of moves
        int row = chessPosition.getRow() + rowModifier;
        int col= chessPosition.getColumn() + colModifier;
        ChessPosition endPosition = new ChessPosition(row, col);
        while(isInsideBoard.insideBoard(endPosition)) {
            if(board.getPiece(endPosition) !=null) {
                if(board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
                    moves.add(new ChessMove(chessPosition, endPosition, null));
                }
                break;
            }
            moves.add(new ChessMove(chessPosition, endPosition, null));

            row+=rowModifier;
            col+=colModifier;
            endPosition = new ChessPosition(row, col);
        }
        return moves;
    }
}