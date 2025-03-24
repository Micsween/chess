package ui;

import client.ClientException;
import client.ServerFacade;
import model.GameData;
import model.UserData;
import server.Server;
import service.responses.CreateGameResponse;
import service.responses.ListGamesResponse;
import service.responses.LoginResponse;
import service.responses.RegisterResponse;

import java.util.*;

public class Main {
    static ServerFacade serverFacade;
    static Server server;
    static String username;
    static String authToken;
    String[] gameIDs;

    public static void main(String[] args) {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("localhost", Integer.toString(port));


        //here is where you add helper functions
        //take an input
        while (true) {
            System.out.println("Welcome to CS ♕ 240 Chess. Type 'help' to get started.");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equals("help")) {
                preLogin();
                //await new input
                if (username != null) {
                    postLogin();
                }
                break;
            }
        }


    }


    enum preLoginCommands {
        register,
        login,
        quit,
        help,
    }

    static Map<String, String[]> preLoginCommandParamMap = Map.of(
            "register", new String[]{"username", "password", "email"},
            "login", new String[]{"username", "password"},
            "quit", new String[]{},
            "help", new String[]{}
    );

    static String printCommandUI() {
        StringBuilder consoleUIBuilder = new StringBuilder();
        for (preLoginCommands key : preLoginCommands.values()) {
            consoleUIBuilder.append(key);
            for (String param : preLoginCommandParamMap.get(key.toString())) {
                consoleUIBuilder.append(" <").append(param).append("> ");
            }
            consoleUIBuilder.append("\n");
        }
        return consoleUIBuilder.toString();
    }


    static void preLogin() {
        System.out.println(printCommandUI());
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] inputs = input.split(" ");
        String command = inputs[0];
        String[] params = Arrays.copyOfRange(inputs, 1, inputs.length);
        System.out.println(Arrays.toString(params));
        switch (command) {
            case "register":
                if (params.length != 3) {
                    System.out.println("\n Please provide all fields. \n");
                    preLogin();
                } else {
                    try {
                        RegisterResponse registerResponse = serverFacade.register(new UserData(params[0], params[1], inputs[2]));
                        username = registerResponse.username();
                        authToken = registerResponse.authToken();
                    } catch (ClientException e) {
                        System.out.println("This user already exists, please try again.");
                        preLogin();
                    }
                }
                break;
            case "login":
                if (params.length != 2) {
                    System.out.println("\n Please provide all fields. \n");
                    preLogin();
                } else {
                    try {
                        LoginResponse loginResponse = serverFacade.login(new UserData(params[0], inputs[1], null));
                        username = loginResponse.username();
                        authToken = loginResponse.authToken();
                    } catch (ClientException e) {
                        System.out.println("Your username or password was incorrect. Please try again.");
                        preLogin();
                    }
                }
                break;
            case "quit":
                return;
            case "help":
                preLogin();
                break;
        }

    }

    //	Lists all the games that currently exist on the server.
    //	Calls the server list API to get all the game data, and
    //	displays the games in a numbered list, including the game name and players (not observers) in the game.
    static void postLogin() {
        System.out.println(printPostCommandUI());
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] inputs = input.split(" ");
        String command = inputs[0];
        String[] params = Arrays.copyOfRange(inputs, 1, inputs.length);
        switch (command) {
            case "create":
                if (params.length != postLoginCommandParamMap.get(command).length) {
                    System.out.println("\n Please provide all fields. \n");
                } else {
                    CreateGameResponse createGameResponse = serverFacade.createGame(params[0], authToken);
                    System.out.println("Game created! Use the command 'list' to see the list of active games.");
                    //save the created game?
                    postLogin();
                }
                break;
            case "list":
                try {
                    ListGamesResponse listGamesResponse = serverFacade.listGames(authToken);
                    //, and displays the games in a numbered list, including the game name and players (not observers) in the game
                    for (GameData game : listGamesResponse.games()) {
                        System.out.println(listGamesResponse.games());

                    }
                    System.out.println(listGamesResponse.games());
                } catch (ClientException e) {
                    System.out.println("You are not authenticated.");
                    preLogin();
                }
                break;
            case "join":
                break;
            case "observe":
                break;
            case "logout":
                if (params.length != postLoginCommandParamMap.get(command).length) {
                    System.out.println("\n Please provide all fields. \n");
                }
                break;
            case "quit":
                return;
            case "help":
                postLogin();
                break;

        }

    }

    enum PostLoginCommand {
        create,
        list,
        join,
        observe,
        logout,
        quit,
        help,
    }

    static Map<String, String[]> postLoginCommandParamMap = Map.of(
            "create", new String[]{"name"},
            "list", new String[]{},
            "join", new String[]{"ID", "WHITE|BLACK"},
            "observe", new String[]{"ID"},
            "logout", new String[]{},
            "quit", new String[]{},
            "help", new String[]{}
    );

    static String printPostCommandUI() {
        StringBuilder consoleUIBuilder = new StringBuilder();
        for (PostLoginCommand key : PostLoginCommand.values()) {
            consoleUIBuilder.append(key);
            for (String param : postLoginCommandParamMap.get(key.toString())) {
                consoleUIBuilder.append(" <").append(param).append("> ");
            }
            consoleUIBuilder.append("\n");
        }
        return consoleUIBuilder.toString();
    }

    //prelogin function >> just awaits input
    //register handler
    //login handler
    //quit handler
    //help heandler
    //postlogin function
    //gameplay function
}