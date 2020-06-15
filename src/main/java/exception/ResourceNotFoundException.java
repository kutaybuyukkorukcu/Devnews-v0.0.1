package exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4212523L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Throwable e) {
        super(e);
    }

    public ResourceNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}