package ru.vk.competition.minbenchmark.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler({
            WebExchangeBindException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            ServerWebInputException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> handleValidatingException(Exception e) {
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("type", e.getClass().getSimpleName());
        body.put("message", e.getMessage());

        log.error("Intercepted: {}", e.getMessage());
        return ResponseEntity.status(status).body(body);
    }

}
