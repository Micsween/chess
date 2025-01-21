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

        //CASTLING: move a king two squares towards a rook on the same row
        //if moving backward is inside the board, and the next space is empty, or its an enemy piece, its a valid move
        ChessPosition backward = new ChessPosition(chessPosition.getRow() - 1, chessPosition.getColumn());
        if(insideBoard.insideBoard(backward) && ((board.getPiece(backward) == null) || ( board.getPiece(backward) != null && board.getPiece(backward).getTeamColor() != piece.getTeamColor()))) {
            moves.add(new ChessMove(chessPosition, backward, null));
        }
        ChessPosition left = new ChessPosition(chessPosition.getRow(), chessPosition.getColumn() -1 );
        if(insideBoard.insideBoard(left) && ((board.getPiece(left) == null) || ( board.getPiece(left) != null && board.getPiece(left).getTeamColor() != piece.getTeamColor()))) {
            moves.add(new ChessMove(chessPosition, left, null));
        }
        ChessPosition right = new ChessPosition(chessPosition.getRow(), chessPosition.getColumn() + 1 );
        if(insideBoard.insideBoard(right) && ((board.getPiece(right) == null) || ( board.getPiece(right) != null && board.getPiece(right).getTeamColor() != piece.getTeamColor()))) {
            moves.add(new ChessMove(chessPosition, right, null));
        }
        ChessPosition forwardRight = new ChessPosition(chessPosition.getRow() + 1, chessPosition.getColumn() + 1 );
        if(insideBoard.insideBoard(forwardRight) && ((board.getPiece(forwardRight) == null) || ( board.getPiece(forwardRight) != null && board.getPiece(forwardRight).getTeamColor() != piece.getTeamColor()))) {
            moves.add(new ChessMove(chessPosition, forwardRight, null));
        }
        ChessPosition forwardLeft = new ChessPosition(chessPosition.getRow() + 1, chessPosition.getColumn() - 1 );
        if(insideBoard.insideBoard(forwardLeft) && ((board.getPiece(forwardLeft) == null) || ( board.getPiece(forwardLeft) != null && board.getPiece(forwardLeft).getTeamColor() != piece.getTeamColor()))) {
            moves.add(new ChessMove(chessPosition, forwardLeft, null));
        }
        ChessPosition backwardLeft = new ChessPosition(chessPosition.getRow() - 1, chessPosition.getColumn() - 1);
        if(insideBoard.insideBoard(backwardLeft) && ((board.getPiece(backwardLeft) == null) || ( board.getPiece(backwardLeft) != null && board.getPiece(backwardLeft).getTeamColor() != piece.getTeamColor()))) {
            moves.add(new ChessMove(chessPosition, backwardLeft, null));
        }
        ChessPosition backwardRight = new ChessPosition(chessPosition.getRow() - 1, chessPosition.getColumn() + 1);
        if(insideBoard.insideBoard(backwardRight) && ((board.getPiece(backwardRight) == null) || ( board.getPiece(backwardRight) != null && board.getPiece(backwardRight).getTeamColor() != piece.getTeamColor()))) {
            moves.add(new ChessMove(chessPosition,backwardRight, null));
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