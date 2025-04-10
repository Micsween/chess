package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    Object game;

    public LoadGameMessage(ServerMessageType type, Object game) {
        super(type);
        this.game = game;
    }
}
