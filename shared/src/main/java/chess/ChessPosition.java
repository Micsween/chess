package chess;


import java.util.Objects;

public class ChessPosition {
    private final int row;
    private final int col;

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPosition that)) {
            return false;
        }
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the Row of a ChessPosition
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the Column of a ChessPosition
     */
    public int getColumn() {
        return col;
    }
}
