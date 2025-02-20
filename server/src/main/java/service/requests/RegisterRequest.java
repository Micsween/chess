package service.requests;

public record RegisterRequest(
        String username,
        String password,
        String email) {
} //handle exceptions here??

//LoginRequest request = (LoginRequest)gson.fromJson(reqData, LoginRequest.class);
//
//LoginService service = new LoginService();
//LoginResult result = service.login(request);
//
//return gson.toJson(result);