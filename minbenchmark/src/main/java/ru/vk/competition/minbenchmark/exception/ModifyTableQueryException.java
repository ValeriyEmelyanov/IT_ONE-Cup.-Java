package ru.vk.competition.minbenchmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class ModifyTableQueryException extends RuntimeException {
    public ModifyTableQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
