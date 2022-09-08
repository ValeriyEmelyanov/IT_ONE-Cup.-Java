package com.example.r2dbcpreform.controller;

import com.example.r2dbcpreform.repository.TableManagerRepository;
import com.example.r2dbcpreform.ui.request.TableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/table")
@RequiredArgsConstructor
public class TableManagerController {
    private final TableManagerRepository tableManagerRepository;

    @PostMapping
    public Mono<ResponseEntity<Void>> createTable(@RequestBody TableRequest request) {
        return tableManagerRepository.createTable(request)
                .then(Mono.just(new ResponseEntity<>(HttpStatus.CREATED)));
    }

    @GetMapping("/exists/{tablename}")
    public Mono<Boolean> tableExists(@PathVariable("tablename") String tableName) {
        return tableManagerRepository.tableExists(tableName);
    }
}
