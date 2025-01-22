package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    IsInsideBoard isInsideBoard = new IsInsideBoard();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        //while loop: conditions to stop loop:
        //there is a friendly piece (invalid move)
        //there is an enemy piece (still valid, but stop loop)
        //youve reached the end of the board. (valid move, but stop move)
        //FORWARD
        Collection<ChessMove> moves = new ArrayList<>();

        //initialize endPosition to be one more than the current position
        //forward
        int row = chessPosition.getRow() + 1;
        ChessPosition endPosition = new ChessPosition(row, chessPosition.getColumn());
        while(isInsideBoard.insideBoard(endPosition)){
            if(board.getPiece(endPosition) != null){
                if(board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(chessPosition, endPosition, null));
                }
                break;
            }
            //update endPosition
            moves.add(new ChessMove(chessPosition, endPosition, null));
            row++;
            endPosition = new ChessPosition(row, endPosition.getColumn());
        }
        //ADDS ALL THE MOVES TO THE LEFT
        moves.addAll(MoveLeft(board, chessPosition, piece));
        moves.addAll(MoveRight(board, chessPosition, piece));
        moves.addAll(MoveDown(board, chessPosition, piece));
        return moves;
    }

    private Collection<ChessMove> MoveLeft(ChessBoard board, ChessPosition chessPosition, ChessPiece piece){
        Collection<ChessMove> moves = new ArrayList<>();
        int col = chessPosition.getColumn() - 1;
        ChessPosition endPosition = new ChessPosition(chessPosition.getRow(), col);

        while(isInsideBoard.insideBoard(endPosition)){
            if(board.getPiece(endPosition) != null){
                if(board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(chessPosition, endPosition, null));
                }
                break;
            }
            //update endPosition
            moves.add(new ChessMove(chessPosition, endPosition, null));
            col--;
            endPosition = new ChessPosition(endPosition.getRow(), col);
        }
        return moves;
    }
    private Collection<ChessMove> MoveRight(ChessBoard board, ChessPosition chessPosition, ChessPiece piece){
        Collection<ChessMove> moves = new ArrayList<>();
        int col = chessPosition.getColumn() + 1;
        ChessPosition endPosition = new ChessPosition(chessPosition.getRow(), col);

        while(isInsideBoard.insideBoard(endPosition)){
            if(board.getPiece(endPosition) != null){
                if(board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(chessPosition, endPosition, null));
                }
                break;
            }
            //update endPosition
            moves.add(new ChessMove(chessPosition, endPosition, null));
            col++;
            endPosition = new ChessPosition(endPosition.getRow(), col);
        }
        return moves;
    }

    private Collection<ChessMove> MoveDown(ChessBoard board, ChessPosition chessPosition, ChessPiece piece){
        Collection<ChessMove> moves = new ArrayList<>();
        int row = chessPosition.getRow() - 1;
        ChessPosition endPosition = new ChessPosition(row, chessPosition.getColumn());

        while(isInsideBoard.insideBoard(endPosition)){
            if(board.getPiece(endPosition) != null){
                if(board.getPiece(endPosition).getTeamColor() != piece.getTeamColor()){
                    moves.add(new ChessMove(chessPosition, endPosition, null));
                }
                break;
            }
            //update endPosition
            moves.add(new ChessMove(chessPosition, endPosition, null));
            row--;
            endPosition = new ChessPosition(row, endPosition.getColumn());
        }
        return moves;
    }

}


