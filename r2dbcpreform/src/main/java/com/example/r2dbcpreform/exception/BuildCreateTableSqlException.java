package com.example.r2dbcpreform.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class BuildCreateTableSqlException extends RuntimeException {
    public BuildCreateTableSqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
