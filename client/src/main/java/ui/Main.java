package ui;

import chess.*;
import client.ServerFacade;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {
    ServerFacade facade;

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        //here is where you add helper functions
        //take an input
        while (true) {
            System.out.println("Welcome to CS ♕ 240 Chess. Type 'help' to get started.");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equals("help")) {
                preLogin();
                //await new input
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

    static Map<String, String[]> commandParamMap = Map.of(
            "register", new String[]{"username", "password", "email"},
            "login", new String[]{"username", "password"},
            "quit", new String[]{},
            "help", new String[]{}
    );

    static String printCommandUI() {
        Set<String> keySet = commandParamMap.keySet();
        StringBuilder consoleUIBuilder = new StringBuilder();
        for (preLoginCommands key : preLoginCommands.values()) {
            consoleUIBuilder.append(key);
            // + " " + "<" + commandParamMap.get(key) + ">" + "\n")
            for (String param : commandParamMap.get(key.toString())) {
                consoleUIBuilder.append(" <").append(param).append("> ");
            }
            consoleUIBuilder.append("\n");
        }
        return consoleUIBuilder.toString();
    }

    static void preLogin() {
        System.out.println(printCommandUI());
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        switch (command) {
            case "register":
                break;
            case "login":

        }

    }
    //prelogin function >> just awaits input
    //register handler
    //login handler
    //quit handler
    //help heandler
    //postlogin function
    //gameplay function
}