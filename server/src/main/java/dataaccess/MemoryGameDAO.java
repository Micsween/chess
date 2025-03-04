package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    public Collection<GameData> allGameData = new ArrayList<>();

    public void createGame(GameData gameData) throws DataAccessException {
        if (allGameData.contains(gameData)) {
            throw new DataAccessException("Game already exists");
        } else {
            allGameData.add(gameData);
        }
    }

    public GameData getGame(String gameID) {
        for (GameData gameData : allGameData) {
            if (Objects.equals(gameData.gameID(), gameID)) {
                return gameData;
            }
        }
        throw new RuntimeException("Game does not exist");
    }

    public void updateGame(GameData game) {
        GameData gameToUpdate = getGame(game.gameID());
        if (gameToUpdate != null) {
            allGameData.remove(gameToUpdate);
            allGameData.add(game);
        } else {
            throw new RuntimeException("Game does not exist");
        }
    }

    public void joinGame(String username, String playerColor, String gameID) throws AlreadyTakenException, DataAccessException {
        GameData gameToJoin = getGame(gameID);
        if (gameToJoin == null) {
            throw new DataAccessException("Error: bad request");
        }
        allGameData.remove(gameToJoin);
        switch (playerColor) {
            case "WHITE":
                if (gameToJoin.whiteUsername() != null) {
                    throw new AlreadyTakenException();
                }
                allGameData.add(new GameData(gameToJoin.gameID(), username, gameToJoin.blackUsername(), gameToJoin.gameName(), gameToJoin.game()));
                break;
            case "BLACK":
                if (gameToJoin.blackUsername() != null) {
                    throw new AlreadyTakenException();
                }
                allGameData.add(new GameData(gameToJoin.gameID(), gameToJoin.whiteUsername(), username, gameToJoin.gameName(), gameToJoin.game()));
                break;
        }
    }


    public String getColorUsername(String gameID, ChessGame.TeamColor playerColor) {
        GameData game = getGame(gameID);
        if (game != null) {
            return switch (playerColor) {
                case WHITE -> game.whiteUsername();
                case BLACK -> game.blackUsername();
            };
        } else {
            throw new RuntimeException("Game does not exist");
        }
    }

    public Collection<GameData> listGames() {
        return allGameData;
    }

    public void clearAllGames() {
        allGameData.clear();
    }
}
