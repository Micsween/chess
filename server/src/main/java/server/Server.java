package server;

import static dataaccess.DatabaseManager.createDatabase;
import static spark.Spark.*;

import spark.Spark;
import org.eclipse.jetty.websocket.api.annotations.*;


@WebSocket
public class Server {

    
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


