package ru.vk.competition.minbenchmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class SingleQueryNotExistsException500 extends RuntimeException{
    public SingleQueryNotExistsException500(String message) {
        super(message);
    }
}
