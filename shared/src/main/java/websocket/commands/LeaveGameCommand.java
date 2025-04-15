package websocket.commands;

import chess.ChessGame;

public class LeaveGameCommand extends UserGameCommand {
    ChessGame.TeamColor teamColor;

    public LeaveGameCommand(String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(CommandType.LEAVE, authToken, gameID);
        this.teamColor = teamColor;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
