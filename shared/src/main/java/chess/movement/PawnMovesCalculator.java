package chess.movement;
import chess.*;
import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    IsInsideBoard isInsideBoard = new IsInsideBoard();
    //pawn can move forward
    //pawn can hit end
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        //basic move: move forward, can move forward if the endosition is null
        Collection<ChessMove> moves = new ArrayList<>();
        switch (piece.getTeamColor()) {
            case WHITE :
                moves.addAll(forward(board, chessPosition, 1, piece));
                moves.addAll(diagonal(board, chessPosition, 1, -1, piece));
                moves.addAll(diagonal(board, chessPosition, 1, 1, piece));
                break;
             case BLACK:
                //set rowmodifiers
                moves.addAll(forward(board, chessPosition, -1, piece));
                moves.addAll(diagonal(board, chessPosition, -1, -1, piece));
                moves.addAll(diagonal(board, chessPosition, -1, 1, piece));
                break;
        }
        return moves;
    }

    public Collection<ChessMove> promotePiece(ChessPosition chessPosition, ChessPosition endPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.add(new ChessMove(chessPosition, endPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(chessPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(chessPosition, endPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(chessPosition, endPosition, ChessPiece.PieceType.ROOK));
        return moves;
    }
    //can only move forward if the endPosition is empty
    public boolean isFirstMove(ChessPosition chessPosition, ChessPiece piece ){
        boolean firstMove = false;
        switch (piece.getTeamColor()) {
            case WHITE:
                if(chessPosition.getRow() == 2){
                    firstMove = true;
                }
                break;
            case BLACK:
                if(chessPosition.getRow() == 7){
                    firstMove = true;
                };
                break;
        }
        return firstMove;
    }

    public Collection<ChessMove> forward(ChessBoard board, ChessPosition chessPosition, int rowModifier, ChessPiece piece) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = chessPosition.getRow() + rowModifier;
        ChessPosition endPosition = new ChessPosition(row, chessPosition.getColumn());

        if (isInsideBoard.insideBoard(endPosition) && board.getPiece(endPosition) == null) {
            if(row == 8 || row == 1){
                moves.addAll(promotePiece(chessPosition, endPosition));
            }else moves.add(new ChessMove(chessPosition, endPosition, null));
        }
        //first move
        if(isFirstMove(chessPosition, piece) && board.getPiece(endPosition) == null && board.getPiece(new ChessPosition(row+rowModifier, chessPosition.getColumn())) == null) {
            row+=rowModifier;
            endPosition = new ChessPosition(row, chessPosition.getColumn());
            moves.add(new ChessMove(chessPosition, endPosition, null));
        }
        return moves;
    }
    //taking move: move diagonally, can move diagonally if the endPosition is not null and the other piece is of opposite teams color
    public Collection<ChessMove> diagonal(ChessBoard board, ChessPosition chessPosition, int rowModifier, int columnModifier, ChessPiece piece) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = chessPosition.getRow() + rowModifier;
        int col = chessPosition.getColumn() + columnModifier;
        ChessPosition endPosition = new ChessPosition(row, col);

        if(isInsideBoard.insideBoard(endPosition) && board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()) {
            if(row == 8 || row == 1){
                moves.addAll(promotePiece(chessPosition, endPosition));
            }else moves.add(new ChessMove(chessPosition, endPosition, null));
        }
        return moves;
    }
}
