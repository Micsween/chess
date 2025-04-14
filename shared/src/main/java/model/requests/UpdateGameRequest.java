package model.requests;


import model.GameData;

public record UpdateGameRequest(String authToken, GameData gameData) {
}
