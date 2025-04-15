package websocket.messages;

import chess.ChessMove;

public class LoadGameMessage extends ServerMessage {
    Object game;

    public LoadGameMessage(Object game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public Object getGame() {
        return game;
    }
}
