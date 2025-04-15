package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebSocket
public class WSServer {
    AuthDAO authDAO = new DBAuthDAO();
    GameDAO gameDAO = new DBGameDAO();
    Gson gson = new Gson();
    Map<Integer, List<Session>> sessions = new HashMap<>();


    public static void setUp() {
        Spark.webSocket("/ws", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    public void broadcastMessage(int gameId, ServerMessage message) throws IOException {
        //figure oout how to send messages
        //iterate through the gameCode sessions and send a message to each
        getSessions(gameId).forEach(session -> {
            try {
                session.getRemote().sendString(gson.toJson(message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendMessage(RemoteEndpoint recipient, ServerMessage message) {
        try {
            recipient.sendString(gson.toJson(message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcastMessageExcept(int gameId, RemoteEndpoint exception, ServerMessage message) {
        getSessions(gameId).forEach(session -> {
            try {
                if (session.getRemote() != exception) {
                    session.getRemote().sendString(gson.toJson(message));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /// /
//
    public void makeMove(String username, int gameId, RemoteEndpoint endpoint, ChessMove move) {
        try {
            var gameData = gameDAO.getGame(gameId);
            gameData.game().makeMove(move);
            gameDAO.updateGame(gameData);
            broadcastMessage(gameData.gameID(), new LoadGameMessage(gameData.game()));
            broadcastMessageExcept(gameData.gameID(), endpoint, new NotificationMessage(move.prettyPrint(username)));
            if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                broadcastMessage(gameData.gameID(), new NotificationMessage(gameData.blackUsername() + " is in check."));
            }
            if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                broadcastMessage(gameData.gameID(), new NotificationMessage(gameData.blackUsername() + " is in checkmate"));
            }
            if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                broadcastMessage(gameData.gameID(), new NotificationMessage(gameData.whiteUsername() + " is in check."));
            }
            if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                broadcastMessage(gameData.gameID(), new NotificationMessage(gameData.whiteUsername() + " is in checkmate"));
            }
            if (gameData.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
                broadcastMessage(gameData.gameID(), new NotificationMessage("Currently in a stalemate."));
            }

        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidMoveException e) {
            System.out.println("Invalid move exception caught!");
            sendMessage(endpoint, new NotificationMessage("This move is invalid"));
        }
    }

    private void leave(String username, Session session, String message) throws DataAccessException {
        LeaveGameCommand leaveCommand = gson.fromJson(message, LeaveGameCommand.class);
        var gameData = gameDAO.getGame(leaveCommand.getGameID());
        gameDAO.playerLeave(gameData.gameID(), leaveCommand.getTeamColor());
        if (leaveCommand.getTeamColor() == null) {
            broadcastMessageExcept(gameData.gameID(), session.getRemote(), new NotificationMessage("Spectator: " + username + " has left."));
        } else {
            broadcastMessageExcept(gameData.gameID(), session.getRemote(), new NotificationMessage("Player: " + username + " of team: " + leaveCommand.getTeamColor() + " has left."));
        }
    }

    private void resign(String username, Session session, String message) throws DataAccessException {

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.printf("Received: %s\n", message);
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
            AuthData auth = authDAO.getAuth(command.getAuthToken());
            String username = auth.username();
            if (username.isEmpty()) {
                throw new UnauthorizedException();
            }
            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> {
                    System.out.println("connected.");
                    if (username.equals(gameDAO.getGame(command.getGameID()).whiteUsername())) {
                        broadcastMessageExcept(command.getGameID(), session.getRemote(), new NotificationMessage("WHITE: " + username + " connected to the game."));
                    } else if (username.equals(gameDAO.getGame(command.getGameID()).blackUsername())) {
                        broadcastMessageExcept(command.getGameID(), session.getRemote(), new NotificationMessage("BLACK:" + username + " connected to the game."));
                    } else {
                        broadcastMessageExcept(command.getGameID(), session.getRemote(), new NotificationMessage(username + " is now observing the game."));
                    }
                }
                case MAKE_MOVE -> {
                    MakeMoveCommand moveCommand = gson.fromJson(message, MakeMoveCommand.class);
                    makeMove(username, command.getGameID(), session.getRemote(), moveCommand.getMove());
                }
                case LEAVE -> leave(username, session, message);
                case RESIGN -> {
                    GameData gameData = gameDAO.getGame(command.getGameID());
                    gameData.game().setTeamTurn(null);
                    gameDAO.updateGame(gameData);
                    broadcastMessage(gameData.gameID(), new NotificationMessage(username + " has resigned!"));
                    System.out.println("resign.");
                }
            }
        } catch (UnauthorizedException ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage(ex.getMessage()));
        }
    }

    public List<Session> getSessions(int gameID) {
        return sessions.getOrDefault(gameID, null);
    }

    void saveSession(int gameID, Session session) {
        sessions.computeIfAbsent(gameID, k -> new ArrayList<>()).add(session);
    }
}

