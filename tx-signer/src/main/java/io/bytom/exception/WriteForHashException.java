package io.bytom.exception;

public class WriteForHashException extends RuntimeException {

    public WriteForHashException() {
        super();
    }

    public WriteForHashException(String message) {
        super(message);
    }

    public WriteForHashException(String message, Throwable cause) {
        super(message, cause);
    }


    public WriteForHashException(Throwable cause) {
        super(cause);
    }
}
