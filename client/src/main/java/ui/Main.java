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


