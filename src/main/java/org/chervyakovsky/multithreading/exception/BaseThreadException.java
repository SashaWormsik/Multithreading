package org.chervyakovsky.multithreading.exception;

public class BaseThreadException extends Exception {

    public BaseThreadException() {
        super();
    }

    public BaseThreadException(String message) {
        super(message);
    }

    public BaseThreadException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseThreadException(Throwable cause) {
        super(cause);
    }
}