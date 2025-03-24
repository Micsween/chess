package ui;

import chess.*;
import client.ClientException;
import client.ServerFacade;
import model.UserData;
import server.Server;
import service.responses.RegisterResponse;

import java.util.*;

public class Main {
    static ServerFacade serverFacade;
    static Server server;
    static String username;

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

    static Map<String, String[]> commandParamMap = Map.of(
            "register", new String[]{"username", "password", "email"},
            "login", new String[]{"username", "password"},
            "quit", new String[]{},
            "help", new String[]{}
    );

    static String printCommandUI() {
        StringBuilder consoleUIBuilder = new StringBuilder();
        for (preLoginCommands key : preLoginCommands.values()) {
            consoleUIBuilder.append(key);
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
        String input = scanner.nextLine();
        String[] params = input.split(" ");
        String command = params[0];

        switch (command) {
            case "register":
                if (params.length != 4) {
                    System.out.println("\n Please provide all fields. \n");
                    preLogin();
                } else {
                    try {
                        RegisterResponse registerResponse = serverFacade.register(new UserData(params[1], params[2], params[3]));
                        username = registerResponse.username();
                    } catch (ClientException e) {
                        System.out.println("This user already exists, please try again.");
                        preLogin();
                    }
                }
                break;
            case "login":
                if (params.length != 3) {
                    System.out.println("\n Please provide all fields. \n");
                    preLogin();
                } else {
                    try {
                        serverFacade.login(new UserData(params[1], params[2], null));
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

    static void postLogin() {
        System.out.println(printCommandUI());
    }
    //prelogin function >> just awaits input
    //register handler
    //login handler
    //quit handler
    //help heandler
    //postlogin function
    //gameplay function
}