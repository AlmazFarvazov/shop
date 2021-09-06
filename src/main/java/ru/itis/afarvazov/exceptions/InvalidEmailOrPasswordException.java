package ru.itis.afarvazov.exceptions;

public class InvalidEmailOrPasswordException extends RuntimeException{
    public InvalidEmailOrPasswordException() {
        super();
    }

    public InvalidEmailOrPasswordException(String message) {
        super(message);
    }

    public InvalidEmailOrPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEmailOrPasswordException(Throwable cause) {
        super(cause);
    }

    protected InvalidEmailOrPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
