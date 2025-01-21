package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMovesCalculator {
//PUT CODE FOR EACH MOVING PIECE
    //add method for KingMovesCalculator(), QueenMovesCalculator(),, et.c..
   /**
    * pieceMoves: This method is similar to ChessGame.validMoves, except it does
    * not honor whose turn it is or check if the king is being attacked.
    * This method does account for enemy and friendly pieces blocking movement paths.
    * The pieceMoves method will need to take into account the type of piece,
    * and the location of other pieces on the board.
    */
   Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece);
}

