package io.bytom.exception;

public class SerializeTransactionException extends RuntimeException {

    public SerializeTransactionException() {
        super();
    }

    public SerializeTransactionException(String message) {
        super(message);
    }

    public SerializeTransactionException(String message, Throwable cause) {
        super(message, cause);
    }


    public SerializeTransactionException(Throwable cause) {
        super(cause);
    }
}
