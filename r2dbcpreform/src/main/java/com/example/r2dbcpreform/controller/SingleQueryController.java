package com.example.r2dbcpreform.controller;

import com.example.r2dbcpreform.entity.SingleQuery;
import com.example.r2dbcpreform.exception.SingleQueryNotExistsException;
import com.example.r2dbcpreform.repository.SingleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Mono<ResponseEntity<Void>> create(@RequestBody SingleQuery query) {
        return singleQueryRepository.save(query.setAsNew())
                .then(Mono.just(new ResponseEntity<>(HttpStatus.CREATED)));
    }

    @GetMapping
    public Flux<SingleQuery> getAll() {
        return singleQueryRepository.findAll();
    }

/*
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SingleQuery>> getById(@PathVariable("id") Integer queryId) {
        return singleQueryRepository.findById(queryId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
*/

    @GetMapping("/{id}")
    public Mono<SingleQuery> getById(@PathVariable("id") Integer queryId) {
        return singleQueryRepository.findById(queryId)
                .switchIfEmpty(Mono.error(() ->
                        new SingleQueryNotExistsException("Query with id " + queryId + " not found")));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("id") Integer queryId) {
        return singleQueryRepository.findById(queryId)
                .flatMap(q -> singleQueryRepository.deleteById(queryId)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .switchIfEmpty(Mono.error(() ->
                        new SingleQueryNotExistsException("Query with id " + queryId + " not found")));
                //.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE));
    }
}
