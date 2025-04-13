package client;

import websocket.messages.ServerMessage;

public interface ServerMessageHandler {
    default void notify(ServerMessage message) {
        //this will do something  later
    }
}
