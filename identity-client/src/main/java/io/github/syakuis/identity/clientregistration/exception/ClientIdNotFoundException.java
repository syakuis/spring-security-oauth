package io.github.syakuis.identity.clientregistration.exception;

/**
 * @author Seok Kyun. Choi.
 * @since 2022-07-12
 */
public class ClientIdNotFoundException extends RuntimeException {

    public ClientIdNotFoundException() {
        super();
    }

    public ClientIdNotFoundException(String message) {
        super(message);
    }

    public ClientIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientIdNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ClientIdNotFoundException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
