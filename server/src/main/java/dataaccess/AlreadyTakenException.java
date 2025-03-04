package dataaccess;

public class AlreadyTakenException extends Exception {
    public AlreadyTakenException() {
        super("Error: already taken");
    }
}
