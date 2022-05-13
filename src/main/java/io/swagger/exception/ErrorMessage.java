package io.swagger.exception;

import java.util.Date;

public class ErrorMessage {

    private final int statusCode;
    private final Date timestamp;
    private final String message;

    public ErrorMessage(int statusCode, Date timestamp, String message) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
