package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    //define all the methods (actions that game can do)

    /**
     * Creates a game
     * @param gameName a String to represent the name of the game
     * @param Username the username of the player who created the game
     */
    void createGame(String gameName, String Username);

    /**
     * Retrieves GameData
     * @param gameID int; the ID for a game
     * @return GameData
     */
    GameData getGame(int gameID);


    /**
     * Adds a player to a game, given the game exists and the selected team color is not already taken
     * @param username the player joining the game
     * @param playerColor their requested color
     * @param gameID the ID of the game they're attempting to join
     */
    void updateGame(String username, ChessGame.TeamColor playerColor, int gameID);

    /**
     * @param gameID The game's ID
     * @param playerColor The specified team color
     * @return The username of a specified team color in a certain game, or null if there is none.
     */
    String getColorUsername(int gameID, ChessGame.TeamColor playerColor);

    /**
     *
     * @return a collection of all current GameData
     */
    Collection<GameData> listGames();

    /**
     * Deletes ALL current GameData
     */
    void clearAllGames();
}
