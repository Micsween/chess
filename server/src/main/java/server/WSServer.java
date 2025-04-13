package server;

import com.google.gson.Gson;
import dataaccess.DBAuthDAO;
import dataaccess.UnauthorizedException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Map;

@WebSocket
public class WSServer {
    Gson gson = new Gson();
    Map<Integer, Session> sessions;


    public static void setUp() {
        Spark.webSocket("/ws", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    public void sendMessage(RemoteEndpoint endpoint, ServerMessage message) throws IOException {
        //figure oout how to send messages
        //iterate through the gameCode sessions and send a message to each
        endpoint.sendString(gson.toJson(message));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            //um... find a way to get the username
            String username = "potato"; //username(command.getAuthToken());
            // AuthData auth = DBAuthDAO.getAuth(command.getAuthToken());
            if (username.equals("")) {
                throw new UnauthorizedException();
            }
            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> {
                    System.out.println("connected.");
                    sendMessage(session.getRemote(), new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));//connect(session, username, (ConnectCommand) command);
                }
                case MAKE_MOVE ->
                        System.out.println("make a move."); //makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> System.out.println("leave.");//leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> System.out.println("resign.");//resign(session, username, (ResignCommand) command);
            }
        } catch (UnauthorizedException ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage()));
        }
    }

    void saveSession(int gameID, Session session) {
        sessions.put(gameID, session);
    }
}

