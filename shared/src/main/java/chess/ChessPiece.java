package chess;

import chess.movement.KingMovesCalculator;
import chess.movement.PawnMoves;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor teamColor;
    PieceType pieceType;

    @Override
    public String toString() {
        return "ChessPiece{" +
                "teamColor=" + teamColor +
                ", pieceType=" + pieceType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.teamColor = pieceColor;
    }
    /**
     * The various different chess piece options
     */

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN;

    }

    public ChessGame.TeamColor getTeamColor() {
       return teamColor;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    //theres a VIDEOOO about this
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.board[myPosition.getRow()][myPosition.getColumn()];
        switch (piece.getPieceType()){
            case PAWN: return null;
            case KING:
               KingMovesCalculator kingMoves = new KingMovesCalculator();
               return kingMoves.pieceMoves(board, myPosition, piece);
        }
        return null;
        //do a switch statement that calls the caluclato
    }
}
