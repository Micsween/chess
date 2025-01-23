package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    //queen can move any number of spaces forward, backwards, or diagonally
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        return null;
    }
}
