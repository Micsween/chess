package client;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationsMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;


public class Websocket extends Endpoint implements MessageHandler, MessageHandler.Whole<String>, MessageHandler.Partial<String> {
    private Session session;
    Gson gson = new Gson();
    WebsocketObserver observer;

    public Websocket() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler((MessageHandler) this);
    }

    public void registerObserver(WebsocketObserver observer) {
        this.observer = observer;
    }

    public void loadGame(String message) {
        observer.loadGame(message);
    }

    public void onMessage(String message) {
        //System.out.println(message);
        var serverMessage = this.gson.fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case NOTIFICATION:
                var notificationMessage = this.gson.fromJson(message, NotificationsMessage.class);
                System.out.println(notificationMessage.getMessage());
                break;
            case LOAD_GAME:
                //System.out.println("loading game...");
                var loadGameMessage = this.gson.fromJson(message, LoadGameMessage.class);
                loadGame(loadGameMessage.getGame().toString());
                break;
            case ERROR:
        }
    }

    //update this later. not sure how to handle this test case.
    //   boardUI();
    public void onMessage(String message, boolean partial) {
        System.out.println(message);
    }


    public void sendMessage(UserGameCommand command) throws IOException {
        //sends a message to the ws server
        this.session.getBasicRemote().sendText(this.gson.toJson(command));
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameId) throws Exception {
        sendMessage(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId));
    }
}


//should be able to do all the things I expect websocket to do
//such as: all commands.
//and should have a function emit that communicates with the client