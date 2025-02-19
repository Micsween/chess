package service.requests;

import chess.ChessGame;

public record JoinGameRequest(int authToken, ChessGame.TeamColor playerColor, int gameID) {
}
