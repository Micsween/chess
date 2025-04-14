package ui;

import chess.ChessGame;
import client.ClientException;
import client.ServerFacade;
import model.GameData;
import model.responses.ListGamesResponse;

import java.util.*;
import java.util.stream.Collectors;

public class PostLogin {
    ServerFacade server;
    String authToken;
    int gameId;
    ChessGame.TeamColor color;

    PostLogin(ServerFacade serverFacade) {
        server = serverFacade;
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

    Map<String, String[]> postLoginCommandParamMap = Map.of(
            "create", new String[]{"name"},
            "list", new String[]{},
            "join", new String[]{"ID", "WHITE|BLACK"},
            "observe", new String[]{"ID"},
            "logout", new String[]{},
            "quit", new String[]{},
            "help", new String[]{}
    );


    public String printCommandUI() {
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


    String[] getInput() {
        System.out.println(printCommandUI());
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input.split(" ");

    }

    //fix the bug where creating a name with spaces adds brackets an commas. just turn it into a complete string.
    void create(String[] params) {
        if (params.length < 1) {
            System.out.println("\n Please provide all fields. \n");
            return;
        }
        try {
            server.createGame(Arrays.toString(params), authToken);
        } catch (ClientException e) {
            System.out.println("There was an error creating your game.");
        }
        System.out.println("Game created! Use the command 'list' to see the list of active games.");
    }

    void printGameList(ListGamesResponse listGamesResponse) {
        int i = 1;
        for (GameData game : listGamesResponse.games()) {
            System.out.print(i + ". ");
            System.out.print("Game ID:" + game.gameID() + ". ");
            System.out.print("Game name: " + game.gameName() + " | ");
            System.out.println("Players: " + ((game.blackUsername() == null) ? " " : "Black: " + game.blackUsername()) + " "
                    + ((game.whiteUsername() == null) ? " " : "White: " + game.whiteUsername()));
            i++;
        }
    }

    void list() {
        try {
            ListGamesResponse listGamesResponse = server.listGames(authToken);
            printGameList(listGamesResponse);
        } catch (ClientException e) {
            System.out.println("You are not authenticated.");
        }
    }

    boolean join(String[] params) {
        if (params.length < 1) {
            System.out.println("\n Please provide all fields. \n");
            return false;
        }
        if (!Objects.equals(params[1], "WHITE") && !Objects.equals(params[1], "BLACK")) {
            System.out.println("\n Please provide a valid color. \n");
            return false;
        }
        try {
            gameId = Integer.parseInt(params[0]);
            color = params[1].equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            server.joinPlayer(authToken, color.toString(), gameId);
            return true;
        } catch (ClientException e) {
            System.out.println(params[1] + " is already taken, or the game is full. Please pick a different color, or try 'observe'!");
            return false;
        }
    }

    //update this so that there's an endpoint just to get 1 game.
    boolean observe(String[] params) {
        if (params.length < 1) {
            System.out.println("\n Please provide all fields. \n");
        }
        gameId = Integer.parseInt(params[0]);

        ListGamesResponse listGamesResponse = server.listGames(authToken);
        var foundGame = listGamesResponse.games().stream()
                .filter(game -> game.gameID().equals(gameId))
                .toList();
        if (foundGame.isEmpty()) {
            System.out.println("\n There is no active game with ID " + gameId);
            return false;
        }
        return true;
    }


    public PostLoginResult postLogin(String authToken) {
        this.authToken = authToken;

        while (true) {
            String[] inputs = getInput();
            String command = inputs[0];
            String[] params = Arrays.copyOfRange(inputs, 1, inputs.length);

            switch (command) {
                case "create":
                    create(params);
                    break;
                case "list":
                    list();
                    break;
                case "join":
                    if (join(params)) {
                        return new PostLoginResult(gameId, color, false, false);
                    }
                    break;
                case "observe":
                    if (observe(params)) {
                        return new PostLoginResult(gameId, null, false, false);
                    }
                    break;
                case "logout":
                    server.logout(authToken);
                    return new PostLoginResult(0, null, true, false);
                case "quit":
                    return new PostLoginResult(0, null, false, true);
                case "help":
                    break;
            }

        }

    }
}
