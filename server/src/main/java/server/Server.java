package server;

import static dataaccess.DatabaseManager.createDatabase;
import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.*;
import model.requests.*;
import model.responses.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Response;
import spark.Spark;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;


//@WebSocket
//public class WSServer {
//    public static void main(String[] args) {
//        Spark.port(8080);
//        Spark.webSocket("/ws", WSServer.class);
//        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//    }
//}
@WebSocket
public class Server {


    //put your most important stuff at the top
    //run
    public int run(int desiredPort) {
        try {
            createDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        port(desiredPort);
        staticFiles.location("web");
        WSServer.setUp();
        HttpServer httpServer = new HttpServer();
        httpServer.run();

        init();
        awaitInitialization();

        return port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}


