package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class DBGameDAO implements GameDAO {
    Gson gson = new Gson();

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        String sql;
        sql = "INSERT INTO gamedata (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, gameData.whiteUsername());
            pstmt.setString(2, gameData.blackUsername());
            pstmt.setString(3, gameData.gameName());
            pstmt.setString(4, gson.toJson(gameData.game()));
            pstmt.executeUpdate();

            PreparedStatement getIdStmt = conn.prepareStatement("SELECT LAST_INSERT_ID()");
            ResultSet resultSet = getIdStmt.executeQuery();
            int gameId = 0;
            if (resultSet.next()) {
                gameId = resultSet.getInt(1);
            }
            return new GameData(gameId, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());

        } catch (SQLException ex) {
            throw new DataAccessException("Something was wrong with your request to create a game.");
        }
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(" SELECT * FROM gamedata WHERE gameID=(?);")) {
                pstmt.setInt(1, gameID);
                try (var resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
                        return new GameData(
                                gameID,
                                resultSet.getString("whiteUsername"),
                                resultSet.getString("blackUsername"),
                                resultSet.getString("gameName"),
                                gson.fromJson(resultSet.getString("game"), ChessGame.class)
                        );
                    } else {
                        throw new DataAccessException("Game not found");
                    }
                }
            } catch (Exception e) {
                throw new DataAccessException("Game not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinGame(String username, String playerColor, Integer gameID) throws AlreadyTakenException, DataAccessException {
        GameData gameToJoin = getGame(gameID);
        if (gameToJoin == null) {
            throw new DataAccessException("Error: bad request");
        }
        switch (playerColor) {
            case "WHITE":
                if (gameToJoin.whiteUsername() != null && !gameToJoin.whiteUsername().equals(username)) {
                    throw new AlreadyTakenException();
                }
                updateGame(new GameData(gameID, username, gameToJoin.blackUsername(), gameToJoin.gameName(), gameToJoin.game()));
                break;
            case "BLACK":
                if (gameToJoin.blackUsername() != null && !gameToJoin.blackUsername().equals(username)) {
                    throw new AlreadyTakenException();
                }
                updateGame(new GameData(gameID, gameToJoin.whiteUsername(), username, gameToJoin.gameName(), gameToJoin.game()));
                break;
        }
    }

    public void updateGame(GameData gameData) throws DataAccessException {
        GameData game = getGame(gameData.gameID());
        if (game == null) {
            throw new DataAccessException("Error: bad request");
        }
        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE gamedata " +
                     "SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ?" +
                     "WHERE gameID = ? ;")) {

            pstmt.setString(1, gameData.whiteUsername());
            pstmt.setString(2, gameData.blackUsername());
            pstmt.setString(3, gameData.gameName());
            pstmt.setString(4, gson.toJson(gameData.game()));
            pstmt.setInt(5, gameData.gameID());

            pstmt.executeUpdate();

        } catch (Exception e) {
            throw new DataAccessException("Something went wrong with your request to update a game.");
        }
    }

    @Override
    public Collection<GameData> listGames() {
        Collection<GameData> games = new ArrayList<>();

        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM gamedata;")) {
                try (var resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        //turn this into a function that just converts a resultset into a gameData object
                        GameData gameData = new GameData(
                                resultSet.getInt("gameID"),
                                resultSet.getString("whiteUsername"),
                                resultSet.getString("blackUsername"),
                                resultSet.getString("gameName"),
                                gson.fromJson(resultSet.getString("game"), ChessGame.class)
                        );
                        games.add(gameData);
                    }

                }

            }
        } catch (SQLException | DataAccessException e) {
            System.err.println(e);
        }

        return games;
    }

    @Override
    public void clearAllGames() {
        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("TRUNCATE TABLE gamedata")) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
}
