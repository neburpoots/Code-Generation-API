package io.swagger.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("You are not authorized to make this request.");
    }
}
