package client;

import com.google.gson.Gson;
import model.UserData;
import service.requests.*;
import service.responses.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;


public class ServerFacade {
    Gson gson = new Gson();
    String url;

    //            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    public ServerFacade(String host, String port) {

        this.url = "http://" + host + ":" + port;
    }


    public RegisterResponse register(UserData userData) {
        //passes userData and register url to the web client
        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        return send("/user", "POST", registerRequest, RegisterResponse.class, null);
    }

    public LoginResponse login(UserData userData) {
        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());
        return send("/session", "POST", loginRequest, LoginResponse.class, null);
    }

    public void logout(String authToken) {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        send("/session", "DELETE", logoutRequest, LogoutResponse.class, authToken);
    }

    public ListGamesResponse listGames(String authToken) {
        return send("/game", "GET", null, ListGamesResponse.class, authToken);
    }

    public CreateGameResponse createGame(String gameName, String authToken) {
    }

    public JoinGameResponse joinPlayer(String authToken, String playerColor, Integer gameID) {
    }

    public void clear() {
        this.send("/db", "DELETE", ClearResponse.class);
    }

    //add functionality where send throws an error intead of printing to the console if something happens
    public <T> T send(String path, String method, Object body, Class<T> responseType, String authKey) throws ClientException {
        try {
            URI uri = new URI(url + path);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

            http.setRequestMethod(method);
            if (authKey != null) {
                http.setRequestProperty("authorization", authKey);
            }
            if (body != null) {
                http.setDoOutput(true);
                http.setRequestProperty("Content-Type", "application/json");
                String jsonBody = gson.toJson(body);
                OutputStream outputStream = http.getOutputStream();
                outputStream.write(jsonBody.getBytes());
                outputStream.close();
            }

            http.connect();

            var inputStream = http.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            return gson.fromJson(inputStreamReader, responseType);

        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new ClientException(e.getMessage());
        }

    }

    public <T> T send(String path, String method, Class<T> responseType) throws ClientException {
        return this.send(path, method, null, responseType, null);
    }

}