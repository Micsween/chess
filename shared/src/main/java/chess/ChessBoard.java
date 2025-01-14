package chess;

/**
 *
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
   ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[9][9];
    }


    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getRow()][position.getColumn()] = piece;
    }

    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow()][position.getColumn()];
    }
    public void AddPawns(ChessGame.TeamColor teamColor){
        ChessPiece pawn = new ChessPiece(teamColor, ChessPiece.PieceType.PAWN);
        for(int i = 1; i < 9; i++) {
            ChessPosition position = new ChessPosition(1, i);
            addPiece(position, pawn);
            System.out.println(this.board[1][i].pieceType + "1" + i );
        }

        //creates a new chess piece, a pawn, of a specified color
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    //resets White's side of the board
    /*public void resetWhite(){
        ChessGame.TeamColor teamColor = new ChessGame.TeamColor.WHITE;
        for(ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
            ChessPiece piece = new ChessPiece(teamColor, pieceType);
            switch (pieceType) {
                //ask the TAs
                case PAWN:
                    for (int i = 1; i < 9; i++) {
                        addPiece(new ChessPosition(1, i), piece);
                        //debugging
                        //System.out.println(board[2][i]);
                        //System.out.println(board[0][i].getPieceType());
                        //System.out.println();


                    }
                    break;
                case KNIGHT:
                    //addPiece(new ChessPosition(1,1), piece);
                    //addPiece(new ChessPosition(1, 6), piece);
                case ROOK:
                    //addPiece(new ChessPosition(1,1), piece);
                    //addPiece(new ChessPosition(1,8), piece);
                case BISHOP:
                    //addPiece(new ChessPosition(1, 2), piece);
                    //addPiece(new ChessPosition(1, 5), piece);
                case KING:
                    //addPiece(new ChessPosition(0, 3), piece);
                case QUEEN:
                    addPiece(new ChessPosition(0, 4), piece);
            }
        }
    }
    */
    //resets black's side of the board
    /*public void resetBlack(ChessGame.TeamColor teamColor){
        for(ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
            ChessPiece piece = new ChessPiece(teamColor, pieceType);
            //just updated so that piece changes
            switch (piece.pieceType) {
                case PAWN:
                    for (int i = 0; i < 8; i++) {
                        addPiece(new ChessPosition(6, i), piece);
                    }
                case ROOK:
                    addPiece(new ChessPosition(7,0), piece);
                    addPiece(new ChessPosition(7,7), piece);
                case KNIGHT:
                    addPiece(new ChessPosition(7,1), piece);
                    addPiece(new ChessPosition(7, 6), piece);
                case BISHOP:
                    addPiece(new ChessPosition(7, 2), piece);
                    addPiece(new ChessPosition(7, 5), piece);
                case KING:
                    addPiece(new ChessPosition(7, 3), piece);
                case QUEEN:
                    addPiece(new ChessPosition(7, 4), piece);
            }
        }
    }
    */
    public void resetBoard() {
        AddPawns(ChessGame.TeamColor.WHITE);
        //resets both sides of the board
        //resetWhite(ChessGame.TeamColor.WHITE);
        //resetBlack(ChessGame.TeamColor.BLACK);
    }
}
