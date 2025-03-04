package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    public Collection<GameData> allGameData = new ArrayList<>();

    public void createGame(GameData gameData) throws DataAccessException {
        if (allGameData.contains(gameData)) {
            throw new DataAccessException("Game already exists");
        } else {
            allGameData.add(gameData);
        }
    }

    public GameData getGame(Integer gameID) {
        for (GameData gameData : allGameData) {
            if (gameData.gameID().equals(gameID)) {
                return gameData;
            }
        }
        throw new RuntimeException("Game does not exist");
    }

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

    public void clearAllGames() {
        allGameData.clear();
    }
}
