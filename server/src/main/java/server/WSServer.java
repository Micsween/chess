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
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
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
import java.util.*;

@WebSocket
public class WSServer {
    AuthDAO authDAO = new DBAuthDAO();
    GameDAO gameDAO = new DBGameDAO();
    Gson gson = new Gson();
    static Map<Integer, List<Session>> sessions = new HashMap<>();

    static void clear() {
        sessions = new HashMap<>();
    }

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
                if (session.getRemote() != exception && session.isOpen()) {
                    session.getRemote().sendString(gson.toJson(message));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void makeMove(String username, int gameId, RemoteEndpoint endpoint, ChessMove move) {
        try {
            var gameData = gameDAO.getGame(gameId);
            ChessGame.TeamColor color = null;
            if (username.equals(gameData.whiteUsername())) {
                color = ChessGame.TeamColor.WHITE;
            } else if (username.equals(gameData.blackUsername())) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                sendMessage(endpoint, new ErrorMessage("You are not a player."));
                return;
            }


            var piece = gameData.game().getBoard().getPiece(move.getStartPosition());

            if (piece.getTeamColor() != color) {
                sendMessage(endpoint, new ErrorMessage("This piece does not belong to your team."));
                return;
            }
            gameData.game().makeMove(move);
            gameDAO.updateGame(gameData);
            broadcastMessage(gameData.gameID(), new LoadGameMessage(gameData.game()));
            broadcastMessageExcept(gameData.gameID(), endpoint, new NotificationMessage(move.prettyPrint(username)));

            if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                broadcastMessage(gameData.gameID(), new NotificationMessage(gameData.blackUsername() + " is in checkmate"));
            } else if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                broadcastMessage(gameData.gameID(), new NotificationMessage(gameData.blackUsername() + " is in check."));
            } else if (gameData.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
                broadcastMessage(gameData.gameID(), new NotificationMessage("Currently in a stalemate."));
            } else if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                broadcastMessage(gameData.gameID(), new NotificationMessage(gameData.whiteUsername() + " is in checkmate"));
            } else if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                broadcastMessage(gameData.gameID(), new NotificationMessage(gameData.whiteUsername() + " is in check."));
            }
        } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidMoveException e) {
            System.out.println("Invalid move exception caught!");
            sendMessage(endpoint, new ErrorMessage("This move is invalid"));
        }
    }

    private void leave(String username, Session session, String message) throws DataAccessException {
        LeaveGameCommand leaveCommand = gson.fromJson(message, LeaveGameCommand.class);
        var gameData = gameDAO.getGame(leaveCommand.getGameID());
        ChessGame.TeamColor color = null;
        if (username.equals(gameData.whiteUsername())) {
            color = ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameData.blackUsername())) {
            color = ChessGame.TeamColor.BLACK;
        }

        gameDAO.playerLeave(gameData.gameID(), color);
        if (leaveCommand.getTeamColor() == null) {
            broadcastMessageExcept(gameData.gameID(), session.getRemote(),
                    new NotificationMessage("Spectator: " + username + " has left."));
        } else {
            broadcastMessageExcept(gameData.gameID(), session.getRemote(),
                    new NotificationMessage("Player: " + username + " of team: " + leaveCommand.getTeamColor() + " has left."));
        }
        onClose(session, 200, "You closed the game.");
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
            try {
                gameDAO.getGame(command.getGameID());
            } catch (DataAccessException e) {
                sendMessage(session.getRemote(), new ErrorMessage("This game is already over."));
                return;
            }

            switch (command.getCommandType()) {
                case CONNECT -> {
                    saveSession(command.getGameID(), session);
                    System.out.println("connected.");
                    var game = gameDAO.getGame(command.getGameID());
                    sendMessage(session.getRemote(), new LoadGameMessage(game));
                    if (username.equals(game.whiteUsername())) {
                        broadcastMessageExcept(command.getGameID(), session.getRemote(),
                                new NotificationMessage("WHITE: " + username + " connected to the game."));

                    } else if (username.equals(game.blackUsername())) {
                        broadcastMessageExcept(command.getGameID(), session.getRemote(),
                                new NotificationMessage("BLACK:" + username + " connected to the game."));
                    } else {
                        broadcastMessageExcept(command.getGameID(), session.getRemote(),
                                new NotificationMessage(username + " is now observing the game."));
                    }
                }
                case MAKE_MOVE -> {
                    MakeMoveCommand moveCommand = gson.fromJson(message, MakeMoveCommand.class);
                    makeMove(username, command.getGameID(), session.getRemote(), moveCommand.getMove());
                }
                case LEAVE -> leave(username, session, message);
                case RESIGN -> {
                    var gameData = gameDAO.getGame(command.getGameID());
                    if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
                        sendMessage(session.getRemote(), new ErrorMessage("You are not a player."));
                        return;
                    }
                    gameDAO.deleteGame(command.getGameID());
                    broadcastMessage(command.getGameID(), new NotificationMessage(username + " has resigned!"));
                }
            }
        } catch (UnauthorizedException ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (DataAccessException ex) {
            sendMessage(session.getRemote(), new ErrorMessage("Error: this game does not exist."));
        } catch (Exception ex) {
            ex.printStackTrace();
            //sendMessage(session.getRemote(), new ErrorMessage(ex.getMessage()));
        }
    }

    public List<Session> getSessions(int gameID) {
        return sessions.getOrDefault(gameID, null);
    }

    void saveSession(int gameID, Session session) {
        sessions.computeIfAbsent(gameID, k -> new ArrayList<>()).add(session);
    }


    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.keySet().forEach(k -> sessions.get(k).remove(session));
    }
}

