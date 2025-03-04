package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    //define all the methods (actions that game can do)

    void createGame(GameData gameData) throws DataAccessException;

    /**
     * Retrieves GameData
     *
     * @param gameID int; the ID for a game
     * @return GameData
     * @throws DataAccessException if the game doesn't exist.
     */
    GameData getGame(Integer gameID) throws DataAccessException;

    /**
     * @return a collection of all current GameData
     */
    Collection<GameData> listGames();

    /**
     * Deletes ALL current GameData
     */
    void clearAllGames();
}
