package client;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
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
        this.session.addMessageHandler(this);
    }

    public void registerObserver(WebsocketObserver observer) {
        this.observer = observer;
    }

    public void loadGame(String message) {
        observer.loadGame(message);
    }

    public void onMessage(String message) {
        var serverMessage = this.gson.fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case NOTIFICATION:
                var notificationMessage = this.gson.fromJson(message, NotificationMessage.class);
                System.out.println(notificationMessage.getMessage());
                break;
            case LOAD_GAME:
                var loadGameMessage = this.gson.fromJson(message, LoadGameMessage.class);
                loadGame(loadGameMessage.getGame().toString());
                break;
            case ERROR:
        }
    }

    public void onMessage(String message, boolean partial) {
        System.out.println(message);
    }


    public void sendMessage(UserGameCommand command) throws IOException {
        this.session.getBasicRemote().sendText(this.gson.toJson(command));
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


}
