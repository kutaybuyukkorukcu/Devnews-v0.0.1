package exception;

import javax.naming.AuthenticationException;

// TODO : AuthenticationException (javax.security) yerine RunTimeException mi kullanilmali?
// TODO : AuthenticationException (javax.security)'un avantaji nedir?
public class InvalidJwtAuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 761503632186596342L;

    public InvalidJwtAuthenticationException() {
        super();
    }

    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }

    public InvalidJwtAuthenticationException(Throwable e) {
        super(e);
    }

    public InvalidJwtAuthenticationException(String message, Throwable e) {
        super(message, e);
    }
}
