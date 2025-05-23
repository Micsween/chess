# ♕ BYU CS 240 Chess
This is Michelle's version of Chess! I'm thinking of changing the colors of the pieces. Also, I'm a little nervous as I haven't been able to get IntelliJ to work yet. I'm hoping it works soon!

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIdTUWjFZfF5vD59SYHYsgoU+Ylj0r7lqOH5PF+SwfjAALnAWspqig5QIAePKwvuh6ouisTYgmhgumGbrlCUSAAGaWAatLgaMszQe8JpEuGFocmQACiu7sXA9R2gK2hCiKtGGAAZDADErq60pJpeyHlAagqOs6BLESyHq6pksb+tOIZSeakYctGMBafGcqEcmCEllhPLZrmmDASCSElFcAzCQuk59NOs7NuOrZ9P+V6OYU2Q9jA-aDr0rkju+vnVlOQbefOsVtsuq7eH4gReCg6B7gevjMMe6SZJgIUXkU1DXtInHsfU7HNC0D6qE+3ReY26DtoBZxAiW07QEgABe8SJOUAA8rVzoUmABPUCgANLscZe41DUs3Gbx9QsAtZC7gAklVZD2ZZnUEaheW+phZ1gDhGL4WZGqqaSRgoNwmlBgGCVtWgTFMm6rGlFVXG1cZQaCgA6pt4QLeNOUALwwNDaBKYmFndWmWH5bZCB5kdnYAVccEdZ2pVgH2A4vkunBpeugSQrau7QjAADio6soVp4leezBOdejM1fV9iji1H1zoTKOpqUACQvVQANQ1oKNCP5IdqNsidMDILEmHQtdeFI4RKnMSRMDkmAWnvXWn3fWaEaFJa3K8iZAnCvDwvtQEZAsDUlK7tIMA7fAm1wKtG1bexZBkDtNQAHIAORkMZlIbfUK3sVHAD8MBVN4eg6Aglihob0lOeCwNxtoev3YXj0a2AzOjKosJWyxBmlBxXE8TAABUMDkd4wxMyzBc-UXslOvKA-1xXMldeLe7a2odkObjgX430Av1+MlT9OvKB7ZvACMvYAMwACxPCemQGhWnzTN0Uy56ADZXxBN9TDvUejv8MCNAFFVBV2OQSZhTJr0N+LNN4VG3qOPe5RD6n3PkVfUbkb53z6A-EAT9kFTC+O-T+i5v4UxXJ4dKG5sA+CgNgbg8ANKGDroYNmxViaqxXpUWoDR+aC2CK7Z8vRcGjF-scMWwJyjS1lgQYaMAxrcPyN0CWoE16jg-u+T4BMl7F3Hp6PUsI4A0J1liKeBth6PRNmbBGTdfot3tjGEGTswgIymh7L2Ps-YB24sHTa5Aw4R2jnHBOScU7p0ztnXQech7W2XnJUuwZy4BH9ibGAfpDBvkML3CA-dHbyAMTIB67oYCaMyHQ2EfCUCzGSdiAIfoYh5JoayYAxtoG+xPCAVIMAMjiXaKkNQCSIAJM6eyKaIB2DwxgDcapXpMgwGGLmYgVCd6qAzmgCAYTm62w5HASGCggZ0JgMkDILSd57VvkkYpswYAACEECPxaKDRBIzhKzDgJ09QdCp5CJLDo8ZKBMbYxVtPPGpYwGjBgTAOBJ8CYASJpzUmEV5EHOkAfY+YLCFUwygESwz00LJBgAAKQgDyCe9D0ENg5oA5hf9yjVEThw0YQsLZziHJQ4A6KoBwAgGhKAsw4UCI7G8tMojBriPlpIxWd95GMuZay9lKxwY7Vqi0M5u4FBBxwQ0hFp9VE43UShGAAArPFaBYR6psigNEN0sl6VySYt6ZjlkWK1eUDJwBbXSVWVyHk1iy7yEEi7Ol7UCKVyMZaikhS4XmJdeyN1vI6GCmdnC51+l7UEtMshAN1tyh+C0AU0cNFoolPEpciVbLoCcoaWG-SrrKTYEzbQ0cgpdl+nOZcjB1zbnJNeUBI65RcXGpzFjZWqYInOQBdy-+xNoXkzMJTYh1MAheCZV2L0sBgDYEoYQOWKREEkvMGSodFQAY1Tqq0YwosO2oyuPBX5o8S4gG4HgbRt6swmtwvo-1REq65JvYuxu8abYRpkNVIG3dUn9wUtoWY0awMwDEo69tM9hF5Ifd8-tiFuZph6COyFgDx29GRUAA
