package ui;

import chess.*;
import client.ClientException;
import client.ServerFacade;
import client.Websocket;
import client.WebsocketObserver;
import model.GameData;
import model.responses.ListGamesResponse;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignGameCommand;
import websocket.commands.UserGameCommand;

import java.util.*;

public class Game implements WebsocketObserver {
    String authToken;
    int gameId;
    String username;
    ServerFacade server;
    boolean gameWon = false;
    ChessGame.TeamColor color;
    String winner;
    Websocket websocket;

    Game(ServerFacade serverFacade) {
        server = serverFacade;
    }

    Map<String, Integer> CHAR_TO_COL_MAP = new HashMap<>() {{
        put("a", 1);
        put("b", 2);
        put("c", 3);
        put("d", 4);
        put("e", 5);
        put("f", 6);
        put("g", 7);
        put("h", 8);
    }};


    Map<String, String[]> gameCommandParamMap = Map.of(
            "help", new String[]{},
            "move", new String[]{"start row", "start col", "end col", "end row"},
            "redraw", new String[]{},
            "leave", new String[]{},
            "resign", new String[]{},
            "highlight", new String[]{"row", "col"}
    );

    public void loadGame(String message) {
        //System.out.println("Reloading game because a move was made..");
        //System.out.println(message);
        boardUI();
    }

    public String printCommandUI() {
        StringBuilder consoleUIBuilder = new StringBuilder();
        consoleUIBuilder.append("Available commands:\n");
        for (String key : gameCommandParamMap.keySet()) {
            consoleUIBuilder.append(key);
            for (String param : gameCommandParamMap.get(key)) {
                //System.out.print(param);
                consoleUIBuilder.append(" <").append(param).append("> ");
            }
            consoleUIBuilder.append("\n");
        }
        return consoleUIBuilder.toString();
    }

    String[] getInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input.split(" ");
    }
    

    Collection<ChessPosition> getEndPositions(Collection<ChessMove> moves) {
        var positions = new ArrayList<ChessPosition>();
        moves.forEach(move -> {
            positions.add(move.getEndPosition());
        });
        return positions;
    }

    Collection<ChessMove> getMoves(String[] params) {
        var gameData = getGameData(gameId);
        int row = Integer.parseInt(params[0]);
        int col = CHAR_TO_COL_MAP.get(params[1]);

        return gameData.game().validMoves(new ChessPosition(row, col));

    }

    void highlightMoves(String[] params) {
        if (params.length < 2) {
            System.out.println("Please provide a chess piece to highlight.");
            return;
        }
        if (!params[0].matches("[1-8]") || !params[1].matches("[a-hA-H]")) {
            System.out.println("One of your inputs was incorrect. Please follow the format: 1-8, a-h");
            return;
        }

        var moves = getMoves(params);
        if (moves == null) {
            System.out.println("There is no piece here.");
            return;
        }
        if (moves.isEmpty()) {
            System.out.println("This piece has no valid moves.");
            return;
        }

        boardUI(getEndPositions(moves));
    }

    public void leaveGame() {
        try {
            websocket.sendMessage(new LeaveGameCommand(authToken, gameId, color));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spectateGame(int gameId, String username, String authToken, ChessGame.TeamColor color) {
        this.gameId = gameId;
        this.authToken = authToken;
        this.username = username;
        this.color = color;
        System.out.println("You are now spectating: " +
                getGameData(gameId).whiteUsername() +
                " and " + getGameData(gameId).blackUsername());
        try {
            websocket = new Websocket();
            websocket.registerObserver(this);
            websocket.sendMessage(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        boardUI();
        while (true) {
            System.out.println("Type 'help' to see the list of available commands.");
            var inputs = getInput();
            var command = inputs[0];
            switch (command) {
                case "leave":
                    leaveGame();
                    return;
                case "redraw":
                    boardUI();
                    break;
                case "help":
                    System.out.println("help");
                    System.out.println("leave");
                    System.out.println("redraw");
                    break;
            }

        }


    }

    ChessMove parseMove(String[] params) {
        int startRow = Integer.parseInt(params[0]);
        int startCol = CHAR_TO_COL_MAP.get(params[1]);
        int endRow = Integer.parseInt(params[2]);
        int endCol = CHAR_TO_COL_MAP.get(params[3]);
        return new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), null);
    }

    void makeMove(String[] params) {

        if (params.length < 4) {
            System.out.println("Please provide all fields");
            return;
        }
        GameData gameData = getGameData(gameId);
        ChessGame game = gameData.game();
        ChessMove move;
        if (!game.isTurn(color)) {
            System.out.println("It is not your turn");
            return;
        }
        try {
            move = parseMove(params);
        } catch (Exception e) {
            System.out.println("One of your inputs is invalid. Please try again");
            return;
        }
        //server.updateGame(authToken, new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game));
        try {
            websocket.sendMessage(new MakeMoveCommand(authToken, gameId, move));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playGame(int gameId, String username, String authToken, ChessGame.TeamColor color) {
        this.gameId = gameId;
        this.authToken = authToken;
        this.username = username;
        this.color = color;
        try {
            websocket = new Websocket();
            websocket.registerObserver(this);
            websocket.sendMessage(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //create a websocket connection

        System.out.println("Type 'help' to see the list of available commands.");
        boardUI();

        while (!gameWon) {
            ChessGame game = getGameData(gameId).game();
            if (game.isInCheckmate(color)) {
                gameWon = true;
                winner = (color == ChessGame.TeamColor.BLACK ? "White" : "Black");
                System.out.println("Team " + winner + " has won!");
            }

            var inputs = getInput();
            var command = inputs[0];
            String[] params = Arrays.copyOfRange(inputs, 1, inputs.length);
            switch (command) {
                case "help":
                    System.out.print(printCommandUI());
                    break;
                case "highlight":
                    highlightMoves(params);
                    break;
                case "move":
                    makeMove(params);
                    break;
                case "redraw":
                    boardUI();
                    break;
                case "leave":
                    System.out.println("Leaving game...");
                    leaveGame();
                    return;
                case "resign":
                    System.out.println("Are you sure you'd like to resign? y/n");
                    var confirmation = getInput();
                    if (confirmation.length > 0 && confirmation[0].equals("y")) {
                        System.out.println("Declaring defeat");
                        try {
                            websocket.sendMessage(new ResignGameCommand(authToken, gameId));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;


            }
            if (gameWon) {
                break;
            }
        }

    }


    boolean highlightPosition(Collection<ChessPosition> highlights, char c, ChessPosition currentPosition) {
        if (highlights != null && highlights.contains(currentPosition)) {
            System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
            printBoardSquare(c);
            switchColor();
            return true;
        }
        return false;
    }

    void printBoard(String[] lines, int start, int finish, int modifier, Collection<ChessPosition> highlights) {
        int[] numbers = {8, 7, 6, 5, 4, 3, 2, 1};

        printHeading(start, finish, modifier);
        for (int i = start; i != finish; i += modifier) {
            String line = lines[i];
            int row = numbers[i];
            printHeading(row);
            switchColor();
            for (int col = start; col != finish; col += modifier) {
                char[] characters = line.toCharArray();

                if (!highlightPosition(highlights, characters[col], new ChessPosition(row, col + 1))) {
                    switchColor();
                    printBoardSquare(characters[col]);
                }
            }
            printHeading(row);
            System.out.println();
        }
        printHeading(start, finish, modifier);
    }

    public GameData getGameData(int gameID) {
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

    void boardUI(Collection<ChessPosition> positions) {
        GameData gameData = getGameData(gameId);

        System.out.println("Current turn: " + gameData.game().getTeamTurn());
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);

        ChessBoard chessBoard = gameData.game().getBoard();
        String board = chessBoard.toString();
        String[] lines = board.split("\n");

        if (Objects.equals(username, gameData.blackUsername())) {
            printBoard(lines, 7, -1, -1, positions);
        } else {
            printBoard(lines, 0, 8, 1, positions);
        }
    }

    void boardUI() {
        boardUI(null);
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


    void printHeading(int start, int finish, int modifier) {
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

    void printHeading(int row) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
        System.out.print("\u2009 " + row + "\u2009 ");
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    String backgroundColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;

    void switchColor() {
        backgroundColor = (backgroundColor.equals(EscapeSequences.SET_BG_COLOR_LIGHT_GREY))
                ? EscapeSequences.SET_BG_COLOR_BLACK : EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        System.out.print(backgroundColor);
    }

    void printBoardSquare(char c) {
        if (Character.isUpperCase(c)) {
            System.out.print(WHITE_PIECE_MAP.get(c));
        } else if (Character.isLowerCase(c)) {
            System.out.print(BLACK_PIECE_MAP.get(c));
        } else {
            System.out.print(EscapeSequences.EMPTY);
        }
    }


}
