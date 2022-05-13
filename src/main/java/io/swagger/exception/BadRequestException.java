package io.swagger.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("The request was invalid or cannot be served.");
    }
}
