package chess.movement;


import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    ValidPosition validPosition = new ValidPosition();

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition chessPosition, ChessPiece piece) {
        Collection<ChessMove> moves = new ArrayList<>();
        switch (piece.getTeamColor()) {
            case WHITE:
                moveForward(board, chessPosition, piece, 1, moves);
                moveDiagonal(board, chessPosition, piece, 1, 1, moves);
                moveDiagonal(board, chessPosition, piece, 1, -1, moves);
                break;
            case BLACK:
                moveForward(board, chessPosition, piece, -1, moves);
                moveDiagonal(board, chessPosition, piece, -1, 1, moves);
                moveDiagonal(board, chessPosition, piece, -1, -1, moves);
                break;
        }
        return moves;
    }


    boolean isFirstMove(ChessPosition chessPosition, ChessPiece piece) {
        return switch (piece.getTeamColor()) {
            case WHITE -> chessPosition.getRow() == 2;
            case BLACK -> chessPosition.getRow() == 7;
        };
    }

    void moveForward(ChessBoard board, ChessPosition chessPosition, ChessPiece piece, int rowModifier, Collection<ChessMove> moves) {
        ChessPosition endPosition = new ChessPosition(chessPosition.getRow() + rowModifier, chessPosition.getColumn());
        if (board.getPiece(endPosition) == null) {
            if (movePromotesPiece(endPosition)) {
                promotePiece(chessPosition, endPosition, moves);
            } else {
                moves.add(new ChessMove(chessPosition, endPosition, null));
            }
            ChessPosition firstMovePosition = new ChessPosition(endPosition.getRow() + rowModifier, endPosition.getColumn());
            if (isFirstMove(chessPosition, piece) && board.getPiece(firstMovePosition) == null) {
                moves.add(new ChessMove(chessPosition, firstMovePosition, null));
            }

        }

    }

    void moveDiagonal(ChessBoard board, ChessPosition chessPosition, ChessPiece piece, int rowModifier, int colModifier, Collection<ChessMove> moves) {
        ChessPosition endPosition = new ChessPosition(
                chessPosition.getRow() + rowModifier, chessPosition.getColumn() + colModifier);
        if ((validPosition.isValidPosition(board, endPosition, piece))
                && (validPosition.capturesEnemyPiece(board, endPosition, piece))) {
            if (movePromotesPiece(endPosition)) {
                promotePiece(chessPosition, endPosition, moves);
            } else {
                moves.add(new ChessMove(chessPosition, endPosition, null));
            }
        }
    }

    boolean movePromotesPiece(ChessPosition endPosition) {
        return (endPosition.getRow() == 8 || endPosition.getRow() == 1);
    }

    void promotePiece(ChessPosition chessPosition, ChessPosition endPosition, Collection<ChessMove> moves) {
        moves.add(new ChessMove(chessPosition, endPosition, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(chessPosition, endPosition, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(chessPosition, endPosition, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(chessPosition, endPosition, ChessPiece.PieceType.KNIGHT));

    }
}
