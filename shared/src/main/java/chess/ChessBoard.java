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
public class ChessBoard {
   ChessPiece[][] board;
    // write new to string method
    // write new hash method
    // write new equals method
    //intellij writes it for you and on the github
    //make it so that when it returns the string, instead of the objects it does the pieceType and TeamColor
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

/*
idea to make this code simpler:
rookmap where key is the color and value is an array of defaultposition
or piece map where key is the color and type, and value is an array of default positions
key:  ChessPiece.PieceType.ROOK
value: [1,1], [1,8], [8,1] ,[8,8]

or
//set color BLACK/OR WHITE
foreach piecetype en enum:
//add a piece using the color
//ChessPiece piece = new ChessPiece
//addPiece(new ChessPossition(piecesmap[teamColor, pieceType]),  ),
key: ChessGame.TeamColor.WHITE, ChessPiece.PieceType.Rook
value: [1,1], [1,8]
key: ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK
value: [8,1], [8,8]

how to map an enum to the keys of a map??? and skip one??

 */
    public void AddRooks(){
        //adds white rooks
        addPiece(new ChessPosition(1,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,8),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        //adds black rooks
        addPiece(new ChessPosition(8,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }
    public void AddKnights(){
        addPiece(new ChessPosition(1,2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,7),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        //adds black knights
        addPiece(new ChessPosition(8,2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
    }
    public void AddBishops(){
        addPiece(new ChessPosition(1,3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,6),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        //adds bishops
        addPiece(new ChessPosition(8,3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
    }
    public void AddKings(){
        addPiece(new ChessPosition(1,5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8,5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
    }
    public void AddQueens(){
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
        //PrintChessBoard();

    }
}
