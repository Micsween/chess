package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.Iterator;

public class MoveValidator {
    // checks if a move is within the board and if there is a piece in the way
    //takes a collection of moves and removes any invalid moves
    // invalid move: there is an allied piece in the way
    // the move  is outside of the board
    public Collection<ChessMove> ValidateMoves(ChessBoard board, Collection<ChessMove> chessMoves, ChessPiece piece) {
        Iterator<ChessMove> moveIterator = chessMoves.iterator();
        while (moveIterator.hasNext()) {
            ChessPosition position = moveIterator.next().getEndPosition();
            int row = position.getRow();
            int col = position.getColumn();
            ChessPiece adjacentPiece = board.getPiece(position);
            //if it's out of bounds, remove the move from the collection.
            if (row >= 9 || row <= 0 || col >= 9 || col <= 0) {
                moveIterator.remove();
            } else if (adjacentPiece != null && adjacentPiece.getTeamColor() == piece.getTeamColor()) {
                //if the adjacent piece is the same color as the current piece, the move is invalid
                moveIterator.remove();
                //if there isnt anything adjacent to the piece, diagonally, it can not move
            }
        }
        return chessMoves;
    }
}
/*
  //checks if these diagonals are within the bounds of the board
            if(diagonalLeft.getRow() < 9  && diagonalLeft.getColumn() > 0 && board.getPiece(diagonalLeft) != null){
                moves.add(new ChessMove(chessPosition, diagonalLeft, null));
            if(diagonalRight.getRow() < 9 && diagonalRight.getColumn() < 9 ) {
                moves.add(new ChessMove(chessPosition, diagonalRight, null));
            }
 */