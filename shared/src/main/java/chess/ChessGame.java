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
    ValidPosition validPosition = new ValidPosition();
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
        for (ChessMove move : pieceMoves) {
            ChessBoard simulatedBoard = chessBoard.clone();
            simulatedBoard.makeMove(move);
            if (isInCheck(piece.getTeamColor(), simulatedBoard)) {
                pieceMoves.remove(move);
            }
        }
        return pieceMoves;
    }
//I know with 100% certainty that validMoves returns a complete collection of valid moves.
// I can use this in my makeMove method!
    /*
    A move is valid if it is a "piece move" for the piece at the input
    location and making that move would not leave the team’s king in danger of check.
    makeMove: Receives a given move and executes it,
    provided it is a legal move. If the move is illegal,
    it throws an InvalidMoveException. A move is illegal if it is not a
    "valid" move for the piece at the starting location, or if it’s not the
     corresponding team's turn.
     */
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    /*
    Make move take own piece
    make move changes team turn
     */
    /*
    * get all the valid moves for that piece and if move is not in that collection its invalid
    * */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //get the team color of the first piece to find out if it matches with the team turn
       ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
       //if it's not their turn, or the piece is null, or the this would put the king in check, ITS INVALID
        chessBoard.removePiece(move.getStartPosition());
        chessBoard.addPiece(move.getEndPosition(), (move.getPromotionPiece() != null) ? new ChessPiece(teamTurn, move.getPromotionPiece()) : piece );
        if(isInCheck(this.teamTurn, this.chessBoard)) {
            //move the piece back if that move was invalid
            chessBoard.removePiece(move.getEndPosition());
            chessBoard.addPiece(move.getStartPosition(), piece);
            throw new InvalidMoveException("You can not make a move that would put your king in check.");
        }
        teamTurn = (teamTurn == TeamColor.BLACK)? TeamColor.WHITE : TeamColor.BLACK;
    }
    //make a copy of the board with this new move.
    //check if the move would put the king in check
    // if the move puts the king in check undo the move.
    //make a new chessmove function that just checks i

/*
if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
 */
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    //isInCheck: Returns true if the specified team’s King could be captured by an opposing piece.
    //could be simplified into just checking the last move
//    public boolean isInCheck(TeamColor teamColor) {
//        ChessPosition kingPosition = chessBoard.FindPiece(new ChessPiece(teamColor, ChessPiece.PieceType.KING));
//        Collection<ChessMove> possibleMoves = AllMoves(teamColor);
//        for(ChessMove move : possibleMoves) {
//            if (move.getEndPosition().equals(kingPosition)) {
//                return true;
//            }
//        }
//        return false;
//    }
    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = board.FindPiece(new ChessPiece(teamColor, ChessPiece.PieceType.KING));
        Collection<ChessMove> possibleMoves = AllMoves(teamColor);
        for(ChessMove move : possibleMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Creates and returns a Collection of all possible chessmoves for a team.
     */
    public Collection<ChessMove> AllMoves(TeamColor teamColor) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (int row = 8; row > 0; row--) {
            for (int col = 1; col < 9; col++) {
                ChessPiece piece = this.chessBoard.board[row][col];
                if (piece != null && piece.getTeamColor() != teamColor) {
                    allMoves.addAll(piece.pieceMoves(this.chessBoard, new ChessPosition(row, col)));
                }
            }
        }
        return allMoves;
    }
    //all possible moves. returns a collection of all the possible moves
    // ANY piece could make
    //key-pair map where the key is a chessPiece and its pair is moves.
    //[piece] : its moves

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
        //#1 theyre in check
        //
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
