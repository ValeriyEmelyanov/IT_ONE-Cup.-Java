package com.example.r2dbcpreform.controller;

import com.example.r2dbcpreform.entity.SingleQuery;
import com.example.r2dbcpreform.repository.SingleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/single-query")
@RequiredArgsConstructor
public class SingleQueryController {

    private final SingleQueryRepository singleQueryRepository;

    @GetMapping("/test")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Ok");
    }

    @PostMapping
    public Mono<SingleQuery> create(@RequestBody SingleQuery query) {
        return singleQueryRepository.save(query.setAsNew());
    }

    @GetMapping
    public Flux<SingleQuery> getAll() {
        return singleQueryRepository.findAll();
    }

}
