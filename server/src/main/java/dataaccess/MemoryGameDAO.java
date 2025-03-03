package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {
    public Collection<GameData> allGameData = new ArrayList<>();

    public static String createGameID() {
        Random random = new Random();
        StringBuilder ID = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomDigit = random.nextInt(10);
            ID.append(randomDigit);
        }
        return ID.toString();
    }

    public void createGame(String gameName) throws DataAccessException {
        GameData game = new GameData(createGameID(), "", "", gameName, new ChessGame());
        if (allGameData.contains(game)) {
            throw new DataAccessException("Game already exists");
        } else {
            allGameData.add(game);
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

    public void joinGame(String username, ChessGame.TeamColor playerColor, String gameID) {
        GameData gameToJoin = getGame(gameID);
        if (gameToJoin != null) {
            allGameData.remove(gameToJoin);
            switch (playerColor) {
                case WHITE:
                    allGameData.add(new GameData(gameToJoin.gameID(), username, gameToJoin.blackUsername(), gameToJoin.gameName(), gameToJoin.game()));
                    break;
                case BLACK:
                    allGameData.add(new GameData(gameToJoin.gameID(), gameToJoin.whiteUsername(), username, gameToJoin.gameName(), gameToJoin.game()));
                    break;
            }
        }
    }


    public String getColorUsername(String gameID, ChessGame.TeamColor playerColor) throws DataAccessException {
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
