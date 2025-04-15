package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    public Collection<GameData> allGameData = new ArrayList<>();

    public GameData createGame(GameData gameData) throws DataAccessException {
        if (allGameData.contains(gameData)) {
            throw new DataAccessException("Game already exists");
        } else {
            allGameData.add(gameData);
        }
        return gameData;
    }

    public GameData getGame(Integer gameID) {
        for (GameData gameData : allGameData) {
            if (gameData.gameID().equals(gameID)) {
                return gameData;
            }
        }
        throw new RuntimeException("Game does not exist");
    }

    //@Override
    public void joinGame(String username, String playerColor, Integer gameID) throws AlreadyTakenException, DataAccessException {
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


    public Collection<GameData> listGames() {
        return allGameData;
    }

    @Override
    public void updateGame(GameData gameData) {
        System.out.println("Not implemented");
    }

    @Override
    public void playerLeave(int gameId, ChessGame.TeamColor color) throws DataAccessException {
        throw new DataAccessException("Not implemented");
        // System.out.println("Not implemented");

    }

    public void clearAllGames() {
        allGameData.clear();
    }
}
