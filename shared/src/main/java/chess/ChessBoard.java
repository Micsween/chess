package chess;

/**
 *
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {

    }


    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    public ChessPiece getPiece(ChessPosition position) {
       return board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    //resets White's side of the board
    public void resetWhite(ChessGame.TeamColor teamColor){
        for(ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
            ChessPiece piece = new ChessPiece(teamColor, pieceType);
            switch (pieceType) {
                case PAWN:
                    for (int i = 0; i < 8; i++) {
                        addPiece(new ChessPosition(1, i), piece);
                    }
                case KNIGHT:
                    addPiece(new ChessPosition(0,1), piece);
                    addPiece(new ChessPosition(0, 6), piece);
                case ROOK:
                    addPiece(new ChessPosition(0,0), piece);
                    addPiece(new ChessPosition(0,7), piece);
                case BISHOP:
                    addPiece(new ChessPosition(0, 2), piece);
                    addPiece(new ChessPosition(0, 5), piece);
                case KING:
                    addPiece(new ChessPosition(0, 3), piece);
                case QUEEN:
                    addPiece(new ChessPosition(0, 4), piece);
            }
        }
    }
    //resets black's side of the board
    public void resetBlack(ChessGame.TeamColor teamColor){
        for(ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
            ChessPiece piece = new ChessPiece(teamColor, pieceType);
            switch (pieceType) {
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
    public void resetBoard() {
        //resets both sides of the board
        resetWhite(ChessGame.TeamColor.WHITE);
        resetBlack(ChessGame.TeamColor.BLACK);
    }
}
