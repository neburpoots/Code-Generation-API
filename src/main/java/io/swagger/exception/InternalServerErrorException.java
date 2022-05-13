package io.swagger.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException() {
        super("Internal server error.");
    }
}