package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DBAuthDAO;
import dataaccess.DBGameDAO;
import dataaccess.DBUserDAO;
import dataaccess.ServerDaos;
import model.requests.*;
import model.responses.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Response;

import static spark.Spark.*;

public class HttpServer {
    Gson gson = new Gson();
    ServerDaos daos = new ServerDaos(new DBAuthDAO(), new DBUserDAO(), new DBGameDAO());
    GameService gameService = new GameService(daos);
    UserService userService = new UserService(daos);
    AuthService authService = new AuthService(daos);


    public record GameNameRequest(String gameName) {
    }

    public record JoinGameBody(String playerColor, Integer gameID) {
    }

    public void run() {

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
                HttpServer.GameNameRequest gameNameRequest = gson.fromJson(request.body(), HttpServer.GameNameRequest.class);
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
                HttpServer.JoinGameBody joinGameBody = gson.fromJson(request.body(), HttpServer.JoinGameBody.class);
                JoinGameRequest joinGameRequest = new JoinGameRequest(request.headers("authorization"),
                        joinGameBody.playerColor(), joinGameBody.gameID());
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
}