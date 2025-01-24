package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
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
    IsInsideBoard isInsideBoard = new IsInsideBoard();

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(MoveColumn(board, chessPosition, piece, -1));
        moves.addAll(MoveColumn(board, chessPosition, piece, 1));
        moves.addAll(MoveRow(board, chessPosition, piece, -1));
        moves.addAll(MoveRow(board, chessPosition, piece, 1));
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

}


