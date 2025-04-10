package websocket.messages;

public class NotificationsMessage extends ServerMessage {
    String message;

    public NotificationsMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }
}
