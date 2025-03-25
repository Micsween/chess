package model.responses;

//any time theres an error i can throw a service exception
public class ServiceException extends RuntimeException {
    public ErrorResponse error;

    public ServiceException(int statusCode, String message) {
        super(message);
        error = new ErrorResponse(statusCode, message);
    }
}
