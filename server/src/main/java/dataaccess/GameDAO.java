package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    //define all the methods (actions that game can do)

    GameData createGame(GameData gameData) throws DataAccessException;

    /**
     * Retrieves GameData
     *
     * @param gameID int; the ID for a game
     * @return GameData
     * @throws DataAccessException if the game doesn't exist.
     */
    GameData getGame(Integer gameID) throws DataAccessException;

    void joinGame(String username, String playerColor, Integer gameID) throws AlreadyTakenException, DataAccessException;

    /**
     * @return a collection of all current GameData
     */
    Collection<GameData> listGames();

    void updateGame(GameData gameData) throws DataAccessException;

    void playerLeave(int gameId, ChessGame.TeamColor color) throws DataAccessException;


    /**
     * Deletes ALL current GameData
     */
    void clearAllGames();

    void deleteGame(int gameID);
}
