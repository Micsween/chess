package ui;

import chess.ChessBoard;
import client.ClientException;
import client.ServerFacade;
import model.GameData;
import model.UserData;
import server.Server;
import service.responses.ListGamesResponse;
import service.responses.LoginResponse;
import service.responses.RegisterResponse;

import java.util.*;

public class Main {
    static ServerFacade serverFacade;
    static Server server;
    static String username;
    static String authToken;
    static Boolean quit = false;

    public static void main(String[] args) {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("localhost", Integer.toString(port));

        while (true) {
            System.out.println("Welcome to CS ♕ 240 Chess. Type 'help' to get started.");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equals("help")) {
                preLogin();
                if (username != null) {
                    postLogin();
                }
                if (quit) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                }
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
        switch (command) {
            case "register":
                if (params.length != 3) {
                    System.out.println("\n Please provide all fields. \n");
                    preLogin();
                } else {
                    try {
                        RegisterResponse registerResponse = serverFacade.register(new UserData(params[0], params[1], params[2]));
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
                        //bug where even if password is incorrect it still logs in
                        LoginResponse loginResponse = serverFacade.login(new UserData(params[0], params[1], null));
                        username = loginResponse.username();
                        authToken = loginResponse.authToken();
                    } catch (ClientException e) {
                        System.out.println("Your username or password was incorrect. Please try again.");
                        preLogin();
                    }
                }
                break;
            case "quit":
                quit = true;
                return;
            case "help":
                preLogin();
                break;
        }

    }

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
                    try {
                        serverFacade.createGame(params[0], authToken);
                    } catch (ClientException e) {
                        System.out.println("There was an error creating your game.");
                        postLogin();
                        return;
                    }
                    System.out.println("Game created! Use the command 'list' to see the list of active games.");
                    postLogin();
                }
                break;
            case "list":
                try {
                    ListGamesResponse listGamesResponse = serverFacade.listGames(authToken);
                    //, and displays the games in a numbered list, including the game name and players (not observers) in the game
                    int i = 1;
                    for (GameData game : listGamesResponse.games()) {
                        System.out.print(i + ". ");
                        System.out.print("Game name: " + game.gameName() + " | ");
                        System.out.println("Players: " + ((game.blackUsername() == null) ? " " : game.blackUsername()) + " "
                                + ((game.whiteUsername() == null) ? " " : game.whiteUsername()));
                        i++;
                    }
                    postLogin();
                } catch (ClientException e) {
                    System.out.println("You are not authenticated.");
                    preLogin();
                }
                break;
            case "join":
                try {
                    int gameID = Integer.parseInt(params[0]);
                    serverFacade.joinPlayer(authToken, params[1], gameID);
                    gameplayUI(gameID);
                } catch (ClientException e) {
                    System.out.println(params[1] + " is already taken, or the game is full. Please pick a different color, or try 'observe'!");
                    postLogin();
                }
                break;
            case "observe":
                int gameID = Integer.parseInt(params[0]);
                gameplayUI(gameID);
                break;
            case "logout":
                serverFacade.logout(authToken);
                username = null;
                authToken = null;
                preLogin();
                break;
            case "quit":
                quit = true;
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


    private static final Map<Character, String> blackPieceMap = Map.of(
            'p', EscapeSequences.BLACK_PAWN,
            'n', EscapeSequences.BLACK_KNIGHT,
            'r', EscapeSequences.BLACK_ROOK,
            'q', EscapeSequences.BLACK_QUEEN,
            'k', EscapeSequences.BLACK_KING,
            'b', EscapeSequences.BLACK_BISHOP);


    private static final Map<Character, String> whitePieceMap = Map.of(
            'P', EscapeSequences.WHITE_PAWN,
            'N', EscapeSequences.WHITE_KNIGHT,
            'R', EscapeSequences.WHITE_ROOK,
            'Q', EscapeSequences.WHITE_QUEEN,
            'K', EscapeSequences.WHITE_KING,
            'B', EscapeSequences.WHITE_BISHOP);

    static void printHeading(int start, int finish, int modifier) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
        System.out.print(EscapeSequences.EMPTY);
        char[] headings = "abcdefgh".toCharArray();
        for (int i = start; i != finish; i += modifier) {
            System.out.print("\u2009 " + headings[i] + "\u2009 ");
        }
        System.out.print(EscapeSequences.EMPTY);
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
        System.out.println();
    }

    static void printHeading(char row) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
        System.out.print("\u2009 " + row + "\u2009 ");
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    static String color = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;

    static void switchColor() {
        color = (color.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY)) ? EscapeSequences.SET_BG_COLOR_BLACK : EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        System.out.print(color);
    }

    static void printBoardSquare(char c) {
        if (Character.isUpperCase(c)) {
            System.out.print(whitePieceMap.get(c));
        } else if (Character.isLowerCase(c)) {
            System.out.print(blackPieceMap.get(c));
        } else {
            System.out.print(EscapeSequences.EMPTY);
        }
    }

    static GameData findGameData(int gameID) {
        try {
            ListGamesResponse listResponse = serverFacade.listGames(authToken);
            return listResponse.games().stream().filter(game -> gameID == game.gameID())
                    .findFirst()
                    .orElse(null);
        } catch (ClientException e) {
            System.out.println("You are not authenticated.");
        }
        return null;
    }

    static void gameplayUI(int gameID) {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        GameData gameData = findGameData(gameID);
        if (gameData == null) {
            System.out.println("No game found.");
            preLogin();
            return;
        }
        ChessBoard chessBoard = gameData.game().getBoard();
        String board = chessBoard.toString();
        String[] lines = board.split("\n");

        if (Objects.equals(username, gameData.blackUsername())) {
            printBoard(lines, 7, -1, -1);
        } else {
            printBoard(lines, 0, 8, 1);
        }
    }

    static void printBoard(String[] lines, int start, int finish, int modifier) {
        char[] numbers = "87654321".toCharArray();

        printHeading(start, finish, modifier);
        for (int i = start; i != finish; i += modifier) {
            String line = lines[i];
            char row = numbers[i];
            printHeading(row);
            switchColor();
            for (char c : line.toCharArray()) {
                switchColor();
                printBoardSquare(c);
            }
            printHeading(row);
            System.out.println();
        }
        printHeading(start, finish, modifier);
    }

}


