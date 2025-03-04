package server;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Response;
import spark.Spark;
import service.responses.*;
import service.requests.*;


public class Server {
    Gson gson = new Gson();
    ServerDAOs DAOs = new ServerDAOs(new MemoryAuthDAO(), new MemoryUserDAO(), new MemoryGameDAO());
    GameService gameService = new GameService(DAOs);
    UserService userService = new UserService(DAOs);
    AuthService authService = new AuthService(DAOs);

    public record GameNameRequest(String gameName) {
    }

    public record JoinGameBody(String playerColor, Integer gameID) {
    }

    //put your most important stuff at the top
    public int run(int desiredPort) {
        port(desiredPort);
        staticFiles.location("web");

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
                LogoutRequest logoutRequest = new LogoutRequest(request.headers("authorization"));
                LogoutResponse logoutResponse = userService.logout(logoutRequest);
                return toJson(response, logoutResponse);
            } catch (ServiceException e) {
                return toError(response, e.error);
            }
        });

        delete("/db", (request, response) -> {
            try {
                userService.clear();
                authService.clear();
                ClearResponse clearResponse = gameService.clear();
                return toJson(response, clearResponse);
            } catch (Exception e) {
                return toError(response, new ErrorResponse(500, "Error: " + e.getMessage()));
            }
        });

        post("/game", (request, response) -> {
            try {
                GameNameRequest gameNameRequest = gson.fromJson(request.body(), GameNameRequest.class);
                CreateGameRequest createGameRequest = new CreateGameRequest(request.headers("authorization"), gameNameRequest.gameName());
                CreateGameResponse createGameResponse = gameService.createGame(createGameRequest);
                return toJson(response, createGameResponse);
            } catch (ServiceException e) {
                return toError(response, e.error);
            } catch (Exception e) {
                return toError(response, new ErrorResponse(500, "Error: " + e.getMessage()));
            }
        });

        put("/game", (request, response) -> {
            try {
                request.headers("authorization"); //gets AuthKey
                JoinGameBody joinGameBody = gson.fromJson(request.body(), JoinGameBody.class);
                JoinGameRequest joinGameRequest = new JoinGameRequest(request.headers("authorization"), joinGameBody.playerColor(), joinGameBody.gameID());
                return toJson(response, gameService.joinGame(joinGameRequest));
            } catch (ServiceException e) {
                return toError(response, e.error);
            }
        });


        get("/game", (request, response) -> {
            try {
                ListGamesRequest listGamesRequest = new ListGamesRequest(request.headers("authorization"));
                return toJson(response, gameService.list(listGamesRequest.authToken()));
            } catch (ServiceException e) {
                return toError(response, e.error);
            }
        });

        init();
        awaitInitialization();
        return port();
    }

    String toError(Response response, ErrorResponse errorResponse) {
        response.status(errorResponse.statusCode());
        response.type("application/json");
        return gson.toJson(errorResponse);
    }

    String toJson(Response response, Object body) {
        response.type("application/json");
        return gson.toJson(body);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
