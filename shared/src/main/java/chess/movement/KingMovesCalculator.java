package chess.movement; //this is the directory that my file is in

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
        //testing revised version of code
        int row = chessPosition.getRow();
        int col = chessPosition.getColumn();
        int[][] possibleMoves = new int[][] {{row + 1, col}, {row-1, col}, {row, col-1}, {row, col + 1}, {row + 1 , col + 1}, {row + 1, col - 1}, {row - 1, col - 1}, {row - 1, col + 1}};
        for(int[] possibleMove: possibleMoves){
            //create a new chessmove
            ChessPosition endPosition = new ChessPosition(possibleMove[0], possibleMove[1]);
            // see if this new move is valid
            if(insideBoard.insideBoard(endPosition) && ((board.getPiece(endPosition) == null) || ( board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()))) {
                moves.add(new ChessMove(chessPosition, endPosition, null));
            }
        }

    return moves;
    }
}