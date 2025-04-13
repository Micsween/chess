package ui;

//if color is null, the player is observing
public record PostLoginResult(int gameId, String color, boolean logout, boolean quit) {
}
