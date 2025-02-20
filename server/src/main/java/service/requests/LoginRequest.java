package service.requests;

public record LoginRequest(String username, String password) {

}
//LoginRequest request = (LoginRequest)gson.fromJson(reqData, LoginRequest.class);
//
//LoginService service = new LoginService();
//LoginResult result = service.login(request);
//
//return gson.toJson(result);