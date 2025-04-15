package websocket.messages;

public class NotificationsMessage extends ServerMessage {
    String message;

    public NotificationsMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
