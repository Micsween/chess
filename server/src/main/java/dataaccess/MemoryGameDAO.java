package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    public Collection<GameData> allGameData = new ArrayList<>();

    public void createGame(String gameName) {
        GameData game = new GameData(123, "", "", gameName, new ChessGame());
        if(!allGameData.contains(game)) {
            allGameData.add(game);
        }else {
            throw new RuntimeException("Game already exists");
        }
    };

    public GameData getGame(int gameID) {
        for(GameData gameData : allGameData) {
           if (gameData.gameID() == gameID){
               return gameData;
           }
        }
        throw new RuntimeException("Game does not exist");
    }

    public void updateGame(GameData game) {
        GameData gameToUpdate = getGame(game.gameID());
        if(gameToUpdate != null) {
            allGameData.remove(gameToUpdate);
            allGameData.add(game);
        }else {
            throw new RuntimeException("Game does not exist");
        }
    }

    public void joinGame(String username, ChessGame.TeamColor playerColor, int gameID) {
        GameData gameToJoin = getGame(gameID);
        if(gameToJoin != null) {
            allGameData.remove(gameToJoin);
            switch(playerColor) {
                case WHITE:
                    allGameData.add(new GameData(gameToJoin.gameID(), username, gameToJoin.blackUsername(), gameToJoin.gameName(), gameToJoin.game()));
                    break;
                case BLACK:
                    allGameData.add(new GameData(gameToJoin.gameID(), gameToJoin.whiteUsername(), username, gameToJoin.gameName(), gameToJoin.game()));
                    break;
            }
        }
    }


    public String getColorUsername(int gameID, ChessGame.TeamColor playerColor) throws DataAccessException {
        GameData game = getGame(gameID);
        if(game != null) {
            return switch (playerColor) {
                case WHITE -> game.whiteUsername();
                case BLACK -> game.blackUsername();
            };
        }else{
            throw new RuntimeException("Game does not exist");
        }
    }

    public Collection<GameData> listGames() {
        return allGameData;
    }
    public void clearAllGames() {
        allGameData.clear();
    };
}
