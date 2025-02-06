package chess;

import java.util.Arrays;
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

   //CLONE THE BOARD
   @Override
    public String toString() {
        StringBuilder chessBoard = new StringBuilder();
        for(ChessPiece[] row : board){
            for(ChessPiece piece : row){
                //if its not null call the piece's toString() method
                if(piece != null){
                    chessBoard.append(piece);
                }
                else chessBoard.append(" |");
            }
            chessBoard.append("\n");
        }
        return chessBoard.toString();
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
        ChessPiece piece = this.getPiece(move.getStartPosition());
        this.removePiece(move.getStartPosition());
        this.addPiece(move.getEndPosition(), piece );
    }
    public ChessBoard() {
        board = new ChessPiece[9][9];
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
    public ChessPosition FindPiece(ChessPiece piece) {
        for (int row = 8; row > 0; row--) {
            for (int col = 1; col < 9; col++) {
                if (board[row][col] != null && board[row][col].equals(piece)) {
                    return new ChessPosition(row, col);
                }
            }
        }
        return null;
    }

    public void AddPawns(ChessGame.TeamColor teamColor) {
        int row = 0;
        if (teamColor == ChessGame.TeamColor.WHITE) {
            row = 2;
        }else if (teamColor == ChessGame.TeamColor.BLACK) {
            row = 7;
        }
        for (int i = 1; i < 9; i++) {
            ChessPiece pawn = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
            ChessPosition position = new ChessPosition(row, i);
            addPiece(position, pawn);
        }
    }

    private void AddRooks(){
        //adds white rooks
        addPiece(new ChessPosition(1,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,8),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        //adds black rooks
        addPiece(new ChessPosition(8,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }
    private void AddKnights(){
        addPiece(new ChessPosition(1,2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,7),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        //adds black knights
        addPiece(new ChessPosition(8,2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
    }
    private void AddBishops(){
        addPiece(new ChessPosition(1,3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,6),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        //adds bishops
        addPiece(new ChessPosition(8,3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
    }
    private void AddKings(){
        addPiece(new ChessPosition(1,5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8,5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
    }
    private void AddQueens(){
        addPiece(new ChessPosition(1,4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));

    }
    public void resetBoard() {
        AddPawns(ChessGame.TeamColor.WHITE);
        AddPawns(ChessGame.TeamColor.BLACK);
        AddRooks();
        AddBishops();
        AddKnights();
        AddQueens();
        AddKings();

    }
    //I don't need to throw an exception because this is an override method
    //now clone IS supported because I wrote a method for it
    // So it should never throw a clone not supported error,
    //If I ever threw that error it means that someone has created a bug.
    // We don't want to handle that exception. Just fix the code.
    @Override
    public ChessBoard clone(){
        try {
            return (ChessBoard) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
