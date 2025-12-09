package org.spring.backendspring.config.security.exception;

public class CustomJWTException extends RuntimeException {

    public CustomJWTException(String msg) {
        super(msg);
    }

}
