package chess;


public class ChessPosition {
    private int row;
    private int col;
    public ChessPosition(int row, int col) {
       this.row = row;
       this.col = col;
    }
    //gets the row of a chess position
    public int getRow() {
        return row;
    }

    //gets the column of a chess position
    public int getColumn() {
        return col;
    }
}
