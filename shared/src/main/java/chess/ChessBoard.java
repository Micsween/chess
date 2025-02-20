package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
//the following is a javadoc comment:
/**
 *
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable{
   ChessPiece[][] board;

    private static final Map<ChessPiece.PieceType, Character> TYPE_TO_CHAR_MAP = Map.of(
            ChessPiece.PieceType.PAWN, 'p',
            ChessPiece.PieceType.KNIGHT,'n',
            ChessPiece.PieceType.ROOK, 'r',
            ChessPiece.PieceType.QUEEN, 'q',
            ChessPiece.PieceType.KING, 'k',
            ChessPiece.PieceType.BISHOP, 'b');

   @Override
    public String toString() {
        StringBuilder chessBoardBuilder = new StringBuilder();
        for(int row = 8; row > 0; row--){
            for(int col = 0; col < 9; col++){
                ChessPiece piece = board[row][col];
                if(piece != null){
                    char pieceChar = TYPE_TO_CHAR_MAP.get(piece.getPieceType());
                    switch(piece.getTeamColor()){
                        case WHITE:
                            chessBoardBuilder.append(Character.toUpperCase(pieceChar)).append("|");
                            break;//a lowercase letter to represent the piece
                        case BLACK:
                            chessBoardBuilder.append(pieceChar).append("|");
                            break;
                    }
                }
                else chessBoardBuilder.append(" |");
            }
            chessBoardBuilder.append("\n");
        }
        return chessBoardBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessBoard that)) {
            return false;
        }
       else return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Moves a piece on the board. Assumes the move that it
     * has been given is VALID
     */
    public void makeMove(ChessMove move){
        ChessPiece piece = getPiece(move.getStartPosition());
        this.removePiece(move.getStartPosition());
        this.addPiece(move.getEndPosition(), piece );
    }
    public ChessBoard() {
        board = new ChessPiece[9][9];
    }
    public ChessPiece[][] getBoard() {
      return this.board;
    }

    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getRow()][position.getColumn()] = piece;
    }
    public void removePiece(ChessPosition position) {
        this.board[position.getRow()][position.getColumn()] = null;
    }
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow()][position.getColumn()];
    }
    public ChessPosition getPiecePosition(ChessPiece piece) {
        for (int row = 8; row > 0; row--) {
            for (int col = 1; col < 9; col++) {
                if (board[row][col] != null && board[row][col].equals(piece)) {
                    return new ChessPosition(row, col);
                }
            }
        }
        return null;
    }

    /**
     * Creates and returns a Collection of all possible chess moves for a team.
     */
    public Collection<ChessMove> AllMoves(ChessGame.TeamColor teamColor) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (int row = 8; row > 0; row--) {
            for (int col = 1; col < 9; col++) {
                ChessPiece piece = getPiece(new ChessPosition(row,col));
                if (piece != null && piece.getTeamColor() != teamColor) {
                    allMoves.addAll(piece.pieceMoves(this, new ChessPosition(row, col)));
                }
            }
        }
        return allMoves;
    }
    public void promotePawn(ChessPosition pawnPosition, ChessPiece.PieceType promotion) throws RuntimeException {
        if(getPiece(pawnPosition) == null || getPiece(pawnPosition).getPieceType() != ChessPiece.PieceType.PAWN){
            throw new RuntimeException("Given position does not contain a pawn");
        }
        addPiece(pawnPosition, new ChessPiece(getPiece(pawnPosition).getTeamColor(), promotion));
    }
    public void addPawns(ChessGame.TeamColor teamColor) {
        int row = (teamColor == ChessGame.TeamColor.WHITE)? 2 : 7;
        for (int i = 1; i < 9; i++) {
            ChessPiece pawn = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
            ChessPosition position = new ChessPosition(row, i);
            addPiece(position, pawn);
        }
    }

    private static final Map<ChessPiece.PieceType, int[]> SPECIAL_PIECE_TO_COLUMN_MAP = Map.of(
            ChessPiece.PieceType.KNIGHT,new int[]{2, 7},
            ChessPiece.PieceType.ROOK, new int[]{1,8},
            ChessPiece.PieceType.QUEEN, new int[]{4},
            ChessPiece.PieceType.KING, new int[]{5},
            ChessPiece.PieceType.BISHOP, new int[]{3,6}
    );

    public void addSpecialPieces(){
        for(ChessPiece.PieceType pieceType : SPECIAL_PIECE_TO_COLUMN_MAP.keySet()){
            int[] columns = SPECIAL_PIECE_TO_COLUMN_MAP.get(pieceType);
            for(int col : columns){
                ChessGame.TeamColor pieceColor = ChessGame.TeamColor.WHITE;
                addPiece(new ChessPosition(1, col), new ChessPiece(pieceColor, pieceType));
                pieceColor = ChessGame.TeamColor.BLACK;
                addPiece(new ChessPosition(8, col), new ChessPiece(pieceColor, pieceType));
            }
        }
    }

    public void resetBoard() {
        addPawns(ChessGame.TeamColor.WHITE);
        addPawns(ChessGame.TeamColor.BLACK);
        addSpecialPieces();
    }
    @Override
    public ChessBoard clone(){
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            ChessPiece[][] clonedBoard = new ChessPiece[9][9];
            for(int row = 8; row > 0; row--) {
                for (int col = 0; col < 9; col++) {
                    ChessPiece piece = board[row][col];
                    if (piece != null) {
                        clonedBoard[row][col] = getPiece(new ChessPosition(row, col));
                    }
                }
            }
            clone.board = clonedBoard;
            return clone;
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
