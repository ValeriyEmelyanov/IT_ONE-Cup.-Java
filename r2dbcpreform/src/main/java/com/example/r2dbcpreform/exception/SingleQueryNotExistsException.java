package com.example.r2dbcpreform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class SingleQueryNotExistsException extends RuntimeException{
    public SingleQueryNotExistsException(String message) {
        super(message);
    }
}
