package io.swagger.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Credentials invalid or missing.");
    }
    public UnauthorizedException(String msg) {
        super(msg);
    }
}
