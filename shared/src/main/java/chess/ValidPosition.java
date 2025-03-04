package chess;

import java.util.Collection;

public class ValidPosition {
    boolean insideBoard(ChessPosition position) {
        return (position.getRow() > 0 && position.getRow() < 9 && position.getColumn() > 0 && position.getColumn() < 9);
    }

    public boolean isValidPosition(ChessBoard board, ChessPosition position, ChessPiece piece) {
        return (insideBoard(position) && ((board.getPiece(position) == null || capturesEnemyPiece(board, position, piece))));
    }

    public boolean capturesEnemyPiece(ChessBoard board, ChessPosition position, ChessPiece piece) {
        return (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != piece.getTeamColor());
    }

    public Collection<ChessMove> createPositions(int[][] possiblePositions,
                                                 ChessBoard board, Collection<ChessMove> moves,
                                                 ChessPiece piece, ChessPosition chessPosition) {
        ValidPosition validPosition = new ValidPosition();
        for (int[] possiblePosition : possiblePositions) {
            ChessPosition endPosition = new ChessPosition(possiblePosition[0], possiblePosition[1]);
            if (validPosition.isValidPosition(board, endPosition, piece)) {
                moves.add(new ChessMove(chessPosition, endPosition, null));
            }
        }
        return moves;
    }

}
