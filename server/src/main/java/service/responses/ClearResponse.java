package service.responses;

import com.google.gson.Gson;

public record ClearResponse(int StatusCode, String Message) {
}
//class Response {
//    String message;
//}
//
//class LoginResponse extends Response {
//    String authtoken;
//    String username;
//}
