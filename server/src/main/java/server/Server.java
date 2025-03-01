package server;

import spark.*;


/*
If the web API requires an auth token, the handler can validate the auth token
Might put this logic in a handler base class so it can be shared by multiple handlers or in a Service class that can be shared by multiple services
Deserialize JSON request body to Java request object
Call service class to perform the requested function, passing it the Java request object
Receive Java response object from service
Serialize Java response object to JSON
Send HTTP response back to client with appropriate status code and response body

 */
public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        //some reference to a handler look at the slides
        //handlers do all thejson work

        Spark.post("/db", (request, response) -> "I've mapped the route /db!" );
        /*
           post("/submit", (request, response) -> "Submitted successfully");
         */
        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
