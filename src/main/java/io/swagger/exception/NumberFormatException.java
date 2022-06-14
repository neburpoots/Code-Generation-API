package io.swagger.exception;

public class NumberFormatException extends java.lang.NumberFormatException {
    public NumberFormatException() {
        super("Number/amount entered was not in correct format. ");
    }
    public NumberFormatException(String s) {
        super(s);
    }
}
