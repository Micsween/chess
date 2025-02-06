package chess;

public class ValidPosition {
    boolean insideBoard(ChessPosition position) {
        return (position.getRow() > 0 && position.getRow() < 9 && position.getColumn() > 0 && position.getColumn() < 9);
    }
    boolean isValidPosition(ChessBoard board, ChessPosition position, ChessPiece piece) {
        return (insideBoard(position) && ((board.getPiece(position) == null || capturesEnemyPiece(board, position, piece))));
    }
    boolean capturesEnemyPiece(ChessBoard board, ChessPosition position, ChessPiece piece)
    {
        return (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != piece.getTeamColor());
    }
}