package ui;

import chess.*;
import client.ClientException;
import client.ServerFacade;
import client.Websocket;
import model.GameData;
import model.responses.ListGamesResponse;

import java.util.*;


//game renders and plays a game.
//main handles an exception if there is no game.
public class Game {
    String authToken;
    int gameId;
    String username;
    ServerFacade server;
    boolean gameWon = false;
    ChessGame.TeamColor color;


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
            "make move", new String[]{"start row", "start col", "end col", "end row"},
            "redraw", new String[]{},
            "leave", new String[]{},
            "resign", new String[]{},
            "highlight", new String[]{"row", "col"}
    );

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

    void makeMove() {
        gameWon = true;
    }

    Collection<ChessPosition> getEndPositions(Collection<ChessMove> moves) {
        var positions = new ArrayList<ChessPosition>();
        moves.forEach(move -> {
            int row = move.getEndPosition().getRow();
            int col = (color == ChessGame.TeamColor.BLACK) ? 9 - move.getEndPosition().getColumn() : move.getEndPosition().getColumn();
            positions.add(new ChessPosition(row, col));

        });
        return positions;
    }

    void highlightMoves(String[] params) {
        if (params.length < 2) {
            System.out.println("Please provide a chess piece to highlight. Format: 'q', 'Q', 'QUEEN', 'queen'");
            return;
        }

        GameData gameData = getGameData(gameId);
        int row = Integer.parseInt(params[0]);
        int col = CHAR_TO_COL_MAP.get(params[1]);
        var moves = gameData.game().validMoves(new ChessPosition(row, col));
        if (moves == null) {
            System.out.println("This piece has been captured.");
            return;
        }
        if (moves.isEmpty()) {
            System.out.println("This piece has no valid moves.");
            return;
        }
        System.out.println(moves);
        var positions = getEndPositions(moves);
        boardUI(positions);
        //if color = white
        //System.out.println(printCommandUI());
        //getPiece();
        //get the piece based off of user input
        //
        //String board = board.toString();
        //String[] lines = board.split("\n");
    }

    public void playGame(int gameId, String username, String authToken, ChessGame.TeamColor color) {
        this.gameId = gameId;
        this.authToken = authToken;
        this.username = username;
        this.color = color;
        System.out.println("Type 'help' to see the list of available commands.");

        while (!gameWon) {
            boardUI();
            //print the commands
            var inputs = getInput();
            var command = inputs[0];
            String[] params = Arrays.copyOfRange(inputs, 1, inputs.length);
            switch (command) {
                case "help":
                    System.out.print(printCommandUI());
                    break;
                case "highlight":
                    highlightMoves(params);

                    //add a function that gets a piece
                    //get the chessgame from the database
                    //server.
                    //based on the team, get the position of that piece,

                    //
                    break;
                case "make move":
                    //websocket message
                    makeMove();
                    //make the move
                    //send out a message that tells every one to redraw the oard
                    //redraw the board
                    break;
                case "redraw":
                    break;
                case "leave":
                    //websocket message
                    //remove the player from the game if they're one of the players.
                    //close the websocket connection
                    //move to post login
                    break;
                case "resign":
                    //close the websocket connection
                    //move to post login
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
        printBoard(lines, start, finish, modifier, new ArrayList<>());
    }

    void printBoard(String[] lines, int start, int finish, int modifier, Collection<ChessPosition> highlights) {
        int[] numbers = {8, 7, 6, 5, 4, 3, 2, 1};

        printHeading(start, finish, modifier);
        for (int i = start; i != finish; i += modifier) {
            String line = lines[i];
            int row = numbers[i];
            printHeading(row);
            switchColor();
            for (int col = start; col < line.length(); col += modifier) {
                char[] characters = line.toCharArray();
                ChessPosition currentPosition = new ChessPosition(row, col + 1);
                if (highlights != null && highlights.contains(currentPosition)) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_BLUE);
                    printBoardSquare(characters[col]);
                    switchColor();
                } else {
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
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        GameData gameData = getGameData(gameId);

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
