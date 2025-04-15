package ui;


import chess.ChessGame;
import client.ServerFacade;

import java.util.*;


public class Main {


    static String printIntro() {
        System.out.println("Welcome to CS ♕ 240 Chess. Type 'help' to get started.");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    //login result, username, quit help
    public static void main(String[] args) {
        String username = null;
        String authToken = null;
        int gameId = 0;
        ChessGame.TeamColor color = null;
        //boolean gameStarted = false;


        ServerFacade serverFacade = new ServerFacade("localhost", "8080");
        PreLogin preLogin = new PreLogin(serverFacade);
        PostLogin postLogin = new PostLogin(serverFacade);
        Game game = new Game(serverFacade);
        //game.playGame(1, "a", "1af22abd-dd04-409c-9333-168e7478b2f6", ChessGame.TeamColor.WHITE);
        //game.playGame(27, "b", "03b51e0e-3d23-4e23-a9ce-c95e4bab6e93", ChessGame.TeamColor.BLACK);
        //game.spectateGame(27, "j", "26e539b0-bf22-40cc-bad0-3a9628e76eb6", null);
        String line = "";
        while (!line.equals("help")) {
            line = printIntro();
        }
        while (true) {
            if (username == null || authToken == null) {
                var loginResult = preLogin.preLogin();
                if (loginResult.quit()) {
                    break;
                }
                username = loginResult.username();
                authToken = loginResult.authToken();
            } else if (gameId == 0) {
                var postLoginResult = postLogin.postLogin(authToken);
                if (postLoginResult.quit()) {
                    break;
                }
                if (postLoginResult.logout()) {
                    username = null;
                    authToken = null;
                }
                gameId = postLoginResult.gameId();
                color = postLoginResult.color();
            } else {
                System.out.println("game started");
                if (color == null) {
                    game.spectateGame(gameId, username, authToken, null);
                } else {
                    game.playGame(gameId, username, authToken, color);
                }
                gameId = 0;
            }
        }
        System.out.println("Goodbye!");
        System.exit(0);
    }


}


