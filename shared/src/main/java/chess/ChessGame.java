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
    //ValidPosition validPosition = new ValidPosition();
    //implement lastMove ChessMove
    //might be useful to keep track of where the king is????
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

    /*
    validMoves: Takes as input a position on the chessboard and returns all moves the piece there can legally make.
    If there is no piece at that location, this method returns null.
    A move is valid if it is a "piece move" for the piece at the input location and
     making that move would not leave the team’s king in danger of check.
    */

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
        Collection<ChessMove> invalidMoves = new ArrayList<>();
        for (ChessMove move : pieceMoves) {
            ChessBoard simulatedBoard = chessBoard.clone();
            simulatedBoard.makeMove(move);
            if (isInCheck(piece.getTeamColor(), simulatedBoard)) {
                invalidMoves.add(move);
            }
        }
        pieceMoves.removeAll(invalidMoves);
        return pieceMoves;
    }

    public boolean isTurn(TeamColor teamColor) {
        return (teamTurn == teamColor);
    }
    public void changeTurn() {
        teamTurn = (teamTurn == TeamColor.BLACK)? TeamColor.WHITE : TeamColor.BLACK;
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
        if(validMoves == null || !validMoves.contains(move) || !isTurn(piece.getTeamColor()) || isInCheck(piece.getTeamColor(), chessBoard.clone())) {
            throw new InvalidMoveException("Invalid move: " + move);
        }
        chessBoard.makeMove(move);
        //chessBoard.addPiece(move.getEndPosition(), (move.getPromotionPiece() != null) ? new ChessPiece(teamTurn, move.getPromotionPiece()) : piece );
        if(move.getPromotionPiece() != null) {
            chessBoard.promotePawn(move.getEndPosition(), move.getPromotionPiece());
        }
        this.changeTurn();


        //promote the piece if the move includes a piece to be promoted to
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = this.chessBoard.getPiecePosition(new ChessPiece(teamColor, ChessPiece.PieceType.KING));
        Collection<ChessMove> possibleMoves = this.chessBoard.AllMoves(teamColor);
        for(ChessMove move : possibleMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }
    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = board.getPiecePosition(new ChessPiece(teamColor, ChessPiece.PieceType.KING));
        Collection<ChessMove> possibleMoves = board.AllMoves(teamColor);
        for(ChessMove move : possibleMoves) {
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
    //you can use validMoves to check
    //is in check? if its true it could checkamte
    //does black have a move that could get you out of check?
    //if i make a move will it leave me in check?
    //clone the board, apply the move, and call is in check
    //if you find one where youre not in check you return  false.
    //get all of the moves for a team and check each move on the cloned board
    public boolean isInCheckmate(TeamColor teamColor) {
        return(isInCheck(teamColor) && hasNoMoves(teamColor));
        //#2 there is no way to get out of check. (or the team has no valid moves)
    }
    public boolean hasNoMoves(TeamColor teamColor) {
        for (int row = 8; row > 0; row--) {
            for (int col = 1; col < 9; col++) {
                ChessPiece piece = this.chessBoard.getPiece(new ChessPosition(row, col));
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = this.validMoves((new ChessPosition(row, col)));
                    if(!moves.isEmpty()) {
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
