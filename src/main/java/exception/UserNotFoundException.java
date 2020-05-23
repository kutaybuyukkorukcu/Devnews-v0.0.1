package exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {

    }

    public UserNotFoundException(int id) {
        super("User id: " + id + " not found");
    }
}
