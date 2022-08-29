package ru.vk.competition.minbenchmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class TableQueryNotExistsException500 extends RuntimeException {
    public TableQueryNotExistsException500(String message) {
        super(message);
    }
}
