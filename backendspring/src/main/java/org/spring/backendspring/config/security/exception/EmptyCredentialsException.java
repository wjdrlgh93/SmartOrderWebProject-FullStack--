package org.spring.backendspring.config.security.exception;

import org.springframework.security.core.AuthenticationException;

public class EmptyCredentialsException extends AuthenticationException {

    public EmptyCredentialsException(String msg) {
        super(msg);
        System.out.println(msg + "<<< EmptyCredentialsException");
    }
}
