package io.bytom.exception;

public class MapTransactionException extends RuntimeException {

    public MapTransactionException() {
        super();
    }

    public MapTransactionException(String message) {
        super(message);
    }

    public MapTransactionException(String message, Throwable cause) {
        super(message, cause);
    }


    public MapTransactionException(Throwable cause) {
        super(cause);
    }
}
