package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    ValidPosition validPosition = new ValidPosition();

    //queen can move any number of spaces forward, backwards, or diagonally
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new ArrayList<>();

        moveDiagonal(board, chessPosition, piece, -1, 1, moves);
        moveDiagonal(board, chessPosition, piece, -1, -1, moves);
        moveDiagonal(board, chessPosition, piece, 1, 1, moves);
        moveDiagonal(board, chessPosition, piece, 1, -1, moves);

        moveRow(board, chessPosition, piece, 1, moves);
        moveRow(board, chessPosition, piece, -1, moves);
        moveCol(board, chessPosition, piece, 1, moves);
        moveCol(board, chessPosition, piece, -1, moves);
        return moves;
    }

    void moveDiagonal(ChessBoard board, ChessPosition chessPosition,
                      ChessPiece piece, int rowModifier, int colModifier, Collection<ChessMove> moves) {
        int row = chessPosition.getRow() + rowModifier;
        int col = chessPosition.getColumn() + colModifier;
        ChessPosition endPosition = new ChessPosition(row, col);
        while (validPosition.isValidPosition(board, endPosition, piece)) {
            moves.add(new ChessMove(chessPosition, endPosition, null));
            if (validPosition.capturesEnemyPiece(board, endPosition, piece)) {
                break;
            }
            row += rowModifier;
            col += colModifier;
            endPosition = new ChessPosition(row, col);
        }

    }

    public void moveCol(ChessBoard board, ChessPosition chessPosition,
                        ChessPiece piece, int colModifier, Collection<ChessMove> moves) {
        int col = chessPosition.getColumn() + colModifier;
        ChessPosition endPosition = new ChessPosition(chessPosition.getRow(), col);
        while (validPosition.isValidPosition(board, endPosition, piece)) {
            moves.add(new ChessMove(chessPosition, endPosition, null));
            if (validPosition.capturesEnemyPiece(board, endPosition, piece)) {
                break;
            }
            col += colModifier;
            endPosition = new ChessPosition(chessPosition.getRow(), col);
        }
    }

    public void moveRow(ChessBoard board, ChessPosition chessPosition,
                        ChessPiece piece, int rowModifier,
                        Collection<ChessMove> moves) {
        int row = chessPosition.getRow() + rowModifier;
        ChessPosition endPosition = new ChessPosition(row, chessPosition.getColumn());
        while (validPosition.isValidPosition(board, endPosition, piece)) {
            moves.add(new ChessMove(chessPosition, endPosition, null));
            if (validPosition.capturesEnemyPiece(board, endPosition, piece)) {
                break;
            }
            row += rowModifier;
            endPosition = new ChessPosition(row, chessPosition.getColumn());
        }
    }
}
