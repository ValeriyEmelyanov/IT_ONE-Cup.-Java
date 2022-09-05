package com.example.r2dbcpreform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class CreateTableException extends RuntimeException {
    public CreateTableException(String message, Throwable cause) {
        super(message, cause);
    }
}
