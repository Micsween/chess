package ui;

import chess.ChessGame;

//if color is null, the player is observing
public record PostLoginResult(int gameId, ChessGame.TeamColor color, boolean logout, boolean quit) {
}
