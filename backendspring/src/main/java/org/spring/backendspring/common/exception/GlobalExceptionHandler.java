package org.spring.backendspring.common.exception;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(Map.of(
                        "error", e.getErrorCode().name(),
                        "message", e.getErrorCode()
                                .getMessage()));
    }
}
