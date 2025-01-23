package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    //i think im going to complete scratch this part
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        //the collection of moves, returns empty if there are no possible moves.
        Collection<ChessMove> moves = new ArrayList<>();
        int row = chessPosition.getRow();
        int col = chessPosition.getColumn();
        int rowModifier = ( piece.getTeamColor() == ChessGame.TeamColor.BLACK)? -1 : 1;
        IsInsideBoard isInsideBoard = new IsInsideBoard();
//        ChessPosition[] endPositions = new ChessPosition[4];
//        endPositions[0] = new ChessPosition((row + rowModifier), (col - 1));
//        //3 times, col +1, col -1, col
//        //for loop starting with 1, i++ i >=0
//        for(int colModifier = 1; colModifier >= 0; colModifier--) {
//            endPositions[colModifier]
//        }
//THE TESTS WANT ALL THE POSSIBLE PROMOTION PIECES
        ChessPosition diagonalLeft = new ChessPosition((row + rowModifier), (col - 1));
        if(isInsideBoard.insideBoard(diagonalLeft)){
            ChessPiece adjacentPiece = board.getPiece(diagonalLeft);
            if(adjacentPiece != null && (adjacentPiece.getTeamColor() != piece.getTeamColor())){
                moves.add(new ChessMove(chessPosition, diagonalLeft, null));
            }
        }
//start: 2,4 | end: 1,3
        ChessPosition diagonalRight = new ChessPosition((row + rowModifier), (col + 1));
        if(isInsideBoard.insideBoard(diagonalRight)) {
            ChessPiece adjacentPiece = board.getPiece(diagonalRight);
            if(adjacentPiece != null && (adjacentPiece.getTeamColor() != piece.getTeamColor()) ){
                moves.add(new ChessMove(chessPosition, diagonalRight, null));
            }

        }

        ChessPosition forward = new ChessPosition((row + rowModifier), (col));
        if(!isInsideBoard.insideBoard(diagonalRight) && board.getPiece(forward) == null) {
            moves.add(new ChessMove(chessPosition, forward, null));
        }
        if (row == 1 || row == 7) {
            ChessPosition forwardTwo = new ChessPosition((row + (rowModifier * 2)), (col));
            moves.add(new ChessMove(chessPosition, forwardTwo, null));
        }

        return moves;
    }
}
