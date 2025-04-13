package ui;

import client.ClientException;
import client.ServerFacade;
import model.UserData;
import model.responses.LoginResponse;
import model.responses.RegisterResponse;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;


public class PreLogin {
    String username;
    String authToken;
    ServerFacade server;

    PreLogin(ServerFacade serverFacade) {
        this.server = serverFacade;
    }

    enum PreLoginCommands {
        register,
        login,
        quit,
        help,
    }

    Map<String, String[]> preLoginCommandParamMap = Map.of(
            "register", new String[]{"username", "password", "email"},
            "login", new String[]{"username", "password"},
            "quit", new String[]{},
            "help", new String[]{}
    );


    public String printCommandUI() {
        StringBuilder consoleUIBuilder = new StringBuilder();
        for (PreLoginCommands key : PreLoginCommands.values()) {
            consoleUIBuilder.append(key);
            for (String param : preLoginCommandParamMap.get(key.toString())) {
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

    boolean register(String[] params) {
        if (params.length != 3) {
            System.out.println("\n Please provide all fields. \n");
            return false;
        }
        try {
            RegisterResponse registerResponse = server.register(new UserData(params[0], params[1], params[2]));
            username = registerResponse.username();
            authToken = registerResponse.authToken();
            return true;
        } catch (ClientException e) {
            System.out.println("This user already exists, please try again.");
            return false;
        }

    }

    boolean login(String[] params) {
        if (params.length != 2) {
            System.out.println("\n Please provide all fields. \n");
            return false;
        }
        try {
            LoginResponse loginResponse = server.login(new UserData(params[0], params[1], null));
            username = loginResponse.username();
            authToken = loginResponse.authToken();
            return true;
        } catch (ClientException e) {
            System.out.println("Your username or password was incorrect. Please try again.");
            return false;
        }
    }

    public LoginResult preLogin() {
        while (true) {
            printCommandUI();
            var inputs = getInput();
            var command = inputs[0];
            var params = Arrays.copyOfRange(inputs, 1, inputs.length);

            switch (command) {
                case "register":
                    if (register(params)) {
                        return new LoginResult(username, authToken, false);
                    }
                    break;
                case "login":
                    if (login(params)) {
                        return new LoginResult(username, authToken, false);
                    }
                    break;
                case "quit":
                    return new LoginResult(null, null, true);
                case "help":
                    break;
            }

        }

    }

}
