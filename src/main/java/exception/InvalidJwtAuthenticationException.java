package exception;

import javax.naming.AuthenticationException;

// TODO : AuthenticationException (javax.security) yerine RunTimeException mi kullanilmali?
// TODO : AuthenticationException (javax.security)'un avantaji nedir?
public class InvalidJwtAuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 761503632186596342L;

    public InvalidJwtAuthenticationException(String e) {
        super(e);
    }
}
