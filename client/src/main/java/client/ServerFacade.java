package client;

import com.google.gson.Gson;
import model.UserData;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.responses.ClearResponse;
import service.responses.LoginResponse;
import service.responses.LogoutResponse;
import service.responses.RegisterResponse;

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

    public void listGames(String authToken) {
    }

    public void createGame(String gameName, String authToken) {
    }

    public void joinPlayer(String authToken, String username, String playerColor, Integer gameID) {
    }

    public void clear() {
        this.send("/db", "DELETE", null, ClearResponse.class, null);
    }

    //add functionality where send throws an error intead of printing to the console if something happens
    public <T> T send(String path, String method, Object body, Class<T> responseType, String authKey) {
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
            System.err.println("There was an issue with opening a connection: " + e.getMessage());
        }
        return null;

    }

    public <T> T send(String path, String method, Class<T> responseType) {
        return this.send(path, method, null, responseType, null);
    }

}