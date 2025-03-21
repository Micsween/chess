import com.google.gson.Gson;
import model.UserData;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;


public class ServerFacade {
    Gson gson = new Gson();

    //            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    public ServerFacade(String host, String port) {
        try {
            URI uri = new URI("http://localhost:8080/name");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //this.url = new URL("http://" + host + ":" + port);
    }


    public void register(UserData userData) {
        //passes userData and register url to the web client
    }

    public void login(UserData userData) {
    }

    public void logout(String authToken) {
    }

    public void listGames(String authToken) {
    }

    public void createGame(String gameName, String authToken) {
    }

    public void joinPlayer(String authToken, String username, String playerColor, Integer gameID) {
    }

    public void clear() {
    }

    public <T> T send(String path, String method, Object body, Class<T> responseType) {
        try {
            URI uri = new URI("http://localhost:8080" + path);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

            http.setRequestMethod(method);
            if (body != null) {
                http.setDoOutput(true);
                http.getOutputStream().write(body.toString().getBytes());
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
        return this.send(path, method, null, responseType);
    }
}