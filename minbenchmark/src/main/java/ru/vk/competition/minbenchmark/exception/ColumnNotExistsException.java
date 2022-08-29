package ru.vk.competition.minbenchmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class ColumnNotExistsException extends RuntimeException {
    public ColumnNotExistsException(String message) {
        super(message);
    }
}
