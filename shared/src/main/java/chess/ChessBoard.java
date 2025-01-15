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
            //FOR DEBUGGING
            //System.out.println(this.board[row][i].teamColor + "1" + i);
        }
    }
    public void addRooks(){
        //adds white rooks
        addPiece(new ChessPosition(1,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,8),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        //adds black rooks
        addPiece(new ChessPosition(8,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }
    /*
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
        AddPawns(ChessGame.TeamColor.BLACK);
        //resets both sides of the board
        //resetWhite(ChessGame.TeamColor.WHITE);
        //resetBlack(ChessGame.TeamColor.BLACK);
    }
}
