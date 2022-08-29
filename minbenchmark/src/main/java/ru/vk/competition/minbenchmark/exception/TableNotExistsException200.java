package ru.vk.competition.minbenchmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK)
public class TableNotExistsException200 extends RuntimeException {
    public TableNotExistsException200(String message) {
        super(message);
    }
}
