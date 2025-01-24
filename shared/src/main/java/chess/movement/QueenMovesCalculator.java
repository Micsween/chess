package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    IsInsideBoard isInsideBoard = new IsInsideBoard();
    //queen can move any number of spaces forward, backwards, or diagonally
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new ArrayList<>();
        //copy and paste bishop moves
        moves.addAll(moveDiagonal(board, chessPosition, piece , 1, 1));
        moves.addAll(moveDiagonal(board, chessPosition, piece , 1, -1));
        moves.addAll(moveDiagonal(board, chessPosition, piece , -1, 1));
        moves.addAll(moveDiagonal(board, chessPosition, piece , -1, -1));
        //also copy and paste rook moves
        moves.addAll(MoveColumn(board, chessPosition, piece, -1));
        moves.addAll(MoveColumn(board, chessPosition, piece, 1));
        moves.addAll(MoveRow(board, chessPosition, piece, -1));
        moves.addAll(MoveRow(board, chessPosition, piece, 1));
        return moves;
    }
    public Collection<ChessMove> moveDiagonal(ChessBoard board, ChessPosition chessPosition, ChessPiece piece, int rowModifier, int colModifier) {
        Collection<ChessMove> moves = new ArrayList<>();
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

    private Collection<ChessMove> MoveColumn(ChessBoard board, ChessPosition chessPosition, ChessPiece piece, int colModifier){
        Collection<ChessMove> moves = new ArrayList<>();
        int col = chessPosition.getColumn() + colModifier;
        ChessPosition endPosition = new ChessPosition(chessPosition.getRow(), col);

        while(isInsideBoard.insideBoard(endPosition)){
            if(board.getPiece(endPosition) != null){
                if(board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(chessPosition, endPosition, null));
                }
                break;
            }
            moves.add(new ChessMove(chessPosition, endPosition, null));
            col += colModifier;
            endPosition = new ChessPosition(endPosition.getRow(), col);
        }
        return moves;
    }
    private Collection<ChessMove> MoveRow(ChessBoard board, ChessPosition chessPosition, ChessPiece piece, int rowModifier){
        Collection<ChessMove> moves = new ArrayList<>();
        int row = chessPosition.getRow() + rowModifier;
        ChessPosition endPosition = new ChessPosition(row, chessPosition.getColumn());

        while(isInsideBoard.insideBoard(endPosition)){
            if(board.getPiece(endPosition) != null){
                if(board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(chessPosition, endPosition, null));
                }
                break;
            }
            moves.add(new ChessMove(chessPosition, endPosition, null));
            row+= rowModifier;
            endPosition = new ChessPosition(row, endPosition.getColumn());
        }
        return moves;
    }
}
