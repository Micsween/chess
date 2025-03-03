package server;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import service.GameService;
import service.UserService;
import service.requests.ClearRequest;
import service.requests.RegisterRequest;
//import service.responses.ClearResponse;
//import service.responses.ErrorResponse;
//import service.responses.RegisterResponse;
//import service.responses.ServiceException;
import spark.Response;
import spark.Spark;
import service.responses.*;
import service.requests.*;

public class Server {
    Gson gson = new Gson();
    UserService userService = new UserService();
    GameService gameService = new GameService();

    //put your most important stuff at the top
    public int run(int desiredPort) {
        port(desiredPort);
        staticFiles.location("web");

        //some reference to a handler look at the slides
        //handlers do all thejson work
        //wehenever someone makes a post request on the path user, route that request to this handler
        //JSon serializer that accepts a response object


        post("/user", (request, response) -> {
            try {
                RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
                RegisterResponse registerResponse = userService.register(registerRequest);
                return toJson(response, registerResponse);
            } catch (ServiceException e) {
                return toError(response, e.error);
            } catch (JsonSyntaxException e) {
                return toError(response, new ErrorResponse(400, "bad Request"));
            } catch (Exception e) {
                return toError(response, new ErrorResponse(500, "Error: " + e.getMessage()));
            }
        });

        post("/session", (request, response) -> {
            try {
                LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);
                LoginResponse loginResponse = userService.login(loginRequest);
                return toJson(response, loginResponse);
            } catch (ServiceException e) {
                return toError(response, e.error);
            } catch (Exception e) {
                return toError(response, new ErrorResponse(500, "Error: " + e.getMessage()));
            }
        });
        delete("/session", (request, response) -> {
            try {
                String authToken = request.headers("authorization");
                LogoutRequest logoutRequest = new LogoutRequest(request.headers("authorization"));
                LogoutResponse logoutResponse = userService.logout(logoutRequest);
                return toJson(response, logoutResponse);
            } catch (ServiceException e) {
                return toError(response, e.error);
            }
        });

        delete("/db", (request, response) -> {
            try {
                ClearRequest clearRequest = gson.fromJson(request.body(), ClearRequest.class);
                GameService gameService = new GameService();

                UserService userService = new UserService();
                userService.clear();
                ClearResponse clearResponse = gameService.clear();
                return toJson(response, clearResponse);
            } catch (Exception e) {
                return toError(response, new ErrorResponse(500, "Error: " + e.getMessage()));
            }
        });
        post("/game", (request, response) -> {
            try {
                CreateGameRequest createGameRequest = gson.fromJson(request.body(), CreateGameRequest.class);
                CreateGameResponse createGameResponse = gameService.createGame(createGameRequest);
                return toJson(response, createGameResponse);
            } catch (ServiceException e) {
                return toError(response, e.error);
            } catch (Exception e) {
                return toError(response, new ErrorResponse(500, "Error: " + e.getMessage()));
            }
        });

        //This line initializes the server and can be removed once you have a functioning endpoint
        init();
        awaitInitialization();
        return port();
    }

    String toError(Response response, ErrorResponse errorResponse) {
        response.status(errorResponse.statusCode());
        response.type("application/json");
        return gson.toJson(errorResponse);
    }

    //for success
    String toJson(Response response, Object body) {
        response.type("application/json");
        return gson.toJson(body);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
