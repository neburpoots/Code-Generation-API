package io.swagger.exception;

public class ConflictException extends RuntimeException {
    public ConflictException() {
        super("There was a conflict processing your request.");
    }
    public ConflictException(String msg) {
        super(msg);
    }
}
