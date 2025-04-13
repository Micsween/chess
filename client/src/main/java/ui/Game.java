package ui;

import chess.ChessBoard;
import client.ClientException;
import client.ServerFacade;
import client.Websocket;
import model.GameData;
import model.responses.ListGamesResponse;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;


//game renders and plays a game.
//main handles an exception if there is no game.
public class Game {
    String authToken;
    int gameId;
    String username;
    ServerFacade server;
    boolean gameWon = false;

    Game(ServerFacade serverFacade) {
        server = serverFacade;


        try {
            Websocket websocket = new Websocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//make a function that, when provided a piece will get all the moves for a specific piece.
//meaning i can access th start position, making the make move command easier for users.


    Map<String, String[]> gameCommandParamMap = Map.of(
            "help", new String[]{},
            "make move", new String[]{"piece", "end col", "end row"},
            "redraw", new String[]{},
            "leave", new String[]{},
            "resign", new String[]{},
            "highlight", new String[]{"piece"}
    );

    public String printCommandUI() {
        StringBuilder consoleUIBuilder = new StringBuilder();
        for (String key : gameCommandParamMap.keySet()) {
            consoleUIBuilder.append(key);
            for (String param : gameCommandParamMap.get(key)) {
                System.out.println(param);
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

    void makeMove() {
        gameWon = true;
    }

    public void playGame(int gameId, String username, String authToken) {
        this.gameId = gameId;
        this.authToken = authToken;
        this.username = username;
        System.out.println("Type 'help' to see the list of available commands.");
        while (!gameWon) {
            boardUI(gameId);
            //print the commands
            var inputs = getInput();
            var command = inputs[0];
            String[] params = Arrays.copyOfRange(inputs, 1, inputs.length);
            switch (command) {
                case "help":
                    break;
                case "highlight":
                    break;
                case "make move":
                    makeMove();
                    break;
                case "redraw":
                    break;
                case "leave":
                    break;
                case "resign":
                    break;

            }

            //get an input
            //if its your turn you can do certain inputs
            //while its not your turn you cant do others

            //switch handler for inputs
            //if player joins: ws.send(gameCommand, client (user), et.c); which goes to the server and does websocket stuff
            //
            if (gameWon) {
                break;
            }
        }

    }

    void printBoard(String[] lines, int start, int finish, int modifier) {
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

    public GameData findGameData(int gameID) {
        try {
            ListGamesResponse listResponse = server.listGames(authToken);
            return listResponse.games().stream().filter(game -> gameID == game.gameID())
                    .findFirst()
                    .orElse(null);
        } catch (ClientException e) {
            System.out.println("You are not authenticated.");
        }
        return null;
    }

    void boardUI(int gameID) {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        GameData gameData = findGameData(gameID);

        ChessBoard chessBoard = gameData.game().getBoard();
        String board = chessBoard.toString();
        String[] lines = board.split("\n");

        if (Objects.equals(username, gameData.blackUsername())) {
            printBoard(lines, 7, -1, -1);
        } else {
            printBoard(lines, 0, 8, 1);
        }
    }


    private static final Map<Character, String> BLACK_PIECE_MAP = Map.of(
            'p', EscapeSequences.BLACK_PAWN,
            'n', EscapeSequences.BLACK_KNIGHT,
            'r', EscapeSequences.BLACK_ROOK,
            'q', EscapeSequences.BLACK_QUEEN,
            'k', EscapeSequences.BLACK_KING,
            'b', EscapeSequences.BLACK_BISHOP);


    private static final Map<Character, String> WHITE_PIECE_MAP = Map.of(
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
        color = (color.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY))
                ? EscapeSequences.SET_BG_COLOR_BLACK : EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        System.out.print(color);
    }

    static void printBoardSquare(char c) {
        if (Character.isUpperCase(c)) {
            System.out.print(WHITE_PIECE_MAP.get(c));
        } else if (Character.isLowerCase(c)) {
            System.out.print(BLACK_PIECE_MAP.get(c));
        } else {
            System.out.print(EscapeSequences.EMPTY);
        }
    }


}
