import chess.*;
import server.HttpServer;

public class Main {
    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        try{
            //call port(8080)
            //call spark.init();
            //server.setup
            //websocket.websocket
            server.run(8080);
        }catch(Exception e){
            e.printStackTrace();
        }
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}