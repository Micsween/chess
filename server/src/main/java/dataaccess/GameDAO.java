package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    //define all the methods (actions that game can do)

    /**
     * Creates a game
     *
     * @param gameName a String to represent the name of the game
     */
    void createGame(String gameName) throws DataAccessException;

    /**
     * Retrieves GameData
     *
     * @param gameID int; the ID for a game
     * @return GameData
     * @throws DataAccessException if the game doesn't exist.
     */
    GameData getGame(String gameID) throws DataAccessException;


    /**
     * @throws DataAccessException if the game doesn't exist.
     */
    void updateGame(GameData gameData) throws DataAccessException;

    /**
     * @param gameID      The game's ID
     * @param playerColor The specified team color
     * @return The username of a specified team color in a certain game, or null if there is none.
     * @throws DataAccessException if the game doesn't exist.
     */
    String getColorUsername(String gameID, ChessGame.TeamColor playerColor) throws DataAccessException;

    /**
     * @return a collection of all current GameData
     */
    Collection<GameData> listGames();

    /**
     * Deletes ALL current GameData
     */
    void clearAllGames();
}
