package ru.vk.competition.minbenchmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class TableQueryNotExistsException extends RuntimeException {
    public TableQueryNotExistsException(String message) {
        super(message);
    }
}
