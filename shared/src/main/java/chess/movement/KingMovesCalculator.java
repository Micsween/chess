package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{
    IsInsideBoard insideBoard = new IsInsideBoard();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new ArrayList<>();
        //THIS IS ALL FOR A WHITE KING CHESSPIECE

        ChessPosition forward = new ChessPosition(chessPosition.getRow() + 1, chessPosition.getColumn());
        //if moving forward is inside the board, and the next space is empty, or its an enemy piece, its a valid move
        if(insideBoard.insideBoard(forward) && ((board.getPiece(forward) == null) || ( board.getPiece(forward) != null && board.getPiece(forward).getTeamColor() != piece.getTeamColor()))) {
            moves.add(new ChessMove(chessPosition, forward, null));
        }

        ChessPosition backward = new ChessPosition(chessPosition.getRow() - 1, chessPosition.getColumn());
        //if moving backward is inside the board, and the next space is empty, or its an enemy piece, its a valid move
        if(insideBoard.insideBoard(backward) && ((board.getPiece(backward) == null) || ( board.getPiece(backward) != null && board.getPiece(forward).getTeamColor() != piece.getTeamColor()))) {
            moves.add(new ChessMove(chessPosition, backward, null));
        }

        //if moving diagonal left is inside the board, and that space is empty, or its an enemy piece, its a valid move.
//        ChessPosition diagonalLeft
//        if(insideBoard.insideBoard(forward) && ((board.getPiece(forward) == null) || ( board.getPiece(forward) != null && board.getPiece(forward).getTeamColor() != piece.getTeamColor()))) {
//            moves.add(new ChessMove(chessPosition, forward, null));
//        }
        //king can move forward
        //diagonally left
        //or diagonally right
        //unless a friendly piece is in the way
        //and unless it goes outside the board
    return moves;
    }
}