package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * do not change yet
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn;
    ChessBoard chessBoard = new ChessBoard();

    /**
     * Creates an immediately playable board with the pieces in their default locations
     * and the starting player set to WHITE.
     */
    public ChessGame() {
        chessBoard.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = chessBoard.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> pieceMoves = piece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> invalidMoves = getInvalidMoves(pieceMoves, piece);
        pieceMoves.removeAll(invalidMoves);
        return pieceMoves;
    }

    public Collection<ChessMove> getInvalidMoves(Collection<ChessMove> pieceMoves, ChessPiece piece) {
        Collection<ChessMove> invalidMoves = new ArrayList<>();
        for (ChessMove move : pieceMoves) {
            ChessBoard simulatedBoard = chessBoard.clone();
            simulatedBoard.makeMove(move);
            if (isInCheck(piece.getTeamColor(), simulatedBoard)) {
                invalidMoves.add(move);
            }
        }
        return invalidMoves;
    }

    public boolean isTurn(TeamColor teamColor) {
        return (teamTurn == teamColor);
    }

    public void changeTurn() {
        teamTurn = (teamTurn == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
        Collection<ChessMove> validMoves = this.validMoves(move.getStartPosition());
        if (!canMove(move, validMoves, piece)) {
            throw new InvalidMoveException("Invalid move: " + move);
        }
        chessBoard.makeMove(move);
        if (move.getPromotionPiece() != null) {
            chessBoard.promotePawn(move.getEndPosition(), move.getPromotionPiece());
        }
        this.changeTurn();
    }

    public boolean canMove(ChessMove move, Collection<ChessMove> validMoves, ChessPiece piece) {
        return (validMoves != null && validMoves.contains(move)
                && isTurn(piece.getTeamColor()) && !isInCheck(piece.getTeamColor(), chessBoard.clone()));
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = this.chessBoard.getPiecePosition(new ChessPiece(teamColor, ChessPiece.PieceType.KING));
        Collection<ChessMove> possibleMoves = this.chessBoard.allMoves(teamColor);
        for (ChessMove move : possibleMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = board.getPiecePosition(new ChessPiece(teamColor, ChessPiece.PieceType.KING));
        Collection<ChessMove> possibleMoves = board.allMoves(teamColor);
        for (ChessMove move : possibleMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return (isInCheck(teamColor) && hasNoMoves(teamColor));
    }

    public boolean hasNoMoves(TeamColor teamColor) {
        //could replace this with call to AllMoves and a check to valid moves
        for (int row = 8; row > 0; row--) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = this.chessBoard.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = this.validMoves((new ChessPosition(row, col)));
                    if (!moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return hasNoMoves(teamColor) && !isInCheck(teamColor);
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
