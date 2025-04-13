package client;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class Websocket extends Endpoint implements MessageHandler, MessageHandler.Whole<String>, MessageHandler.Partial<String> {
    private Session session;
    Gson gson = new Gson();

    public Websocket() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler((MessageHandler) this);
    }

    public void onMessage(String message) {
        System.out.println(message);
    }

    //update this later. not sure how to handle this test case.
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