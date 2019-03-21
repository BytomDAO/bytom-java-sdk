package io.bytom.offline.exception;

/**
 * Base exception class for the Chain Core SDK.
 */
public class BytomException extends Exception {
    /**
     * Default constructor.
     */
    public BytomException() {
        super();
    }

    /**
     * Initializes exception with its message attribute.
     *
     * @param message error message
     */
    public BytomException(String message) {
        super(message);
    }

    /**
     * Initializes a new exception while storing the original cause.
     *
     * @param message the error message
     * @param cause   the cause of the exception
     */
    public BytomException(String message, Throwable cause) {
        super(message, cause);
    }
}
