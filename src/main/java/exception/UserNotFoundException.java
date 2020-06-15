package exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable e) {
        super(e);
    }

    public UserNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
