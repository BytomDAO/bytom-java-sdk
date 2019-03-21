package io.bytom.offline.exception;

public class SignTransactionException extends RuntimeException {

    public SignTransactionException() {
        super();
    }

    public SignTransactionException(String message) {
        super(message);
    }

    public SignTransactionException(String message, Throwable cause) {
        super(message, cause);
    }


    public SignTransactionException(Throwable cause) {
        super(cause);
    }
}
