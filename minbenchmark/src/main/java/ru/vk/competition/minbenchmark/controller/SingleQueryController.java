package ru.vk.competition.minbenchmark.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.service.SingleQueryService;
import ru.vk.competition.minbenchmark.ui.request.SingleQueryRequest;
import ru.vk.competition.minbenchmark.ui.response.SingleQueryResponse;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/single-query")
@RequiredArgsConstructor
@Validated
public class SingleQueryController {

    private final SingleQueryService singleQueryService;

    @PostMapping("/add-new-query")
    public Mono<ResponseEntity<Void>> addNewQueryToTable(@Valid @RequestBody SingleQueryRequest request) {
        return singleQueryService.addQueryWithQueryId(request);
    }

    @PutMapping({"/modify-single-query", "modify-query"})
    public Mono<ResponseEntity<Void>> modifyQueryInTable(@Valid @RequestBody SingleQueryRequest request) {
        return singleQueryService.updateQueryWithQueryId(request);
    }

    @DeleteMapping("/delete-single-query-by-id/{id}")
    public Mono<ResponseEntity<Void>> deleteTableQueryById(@PathVariable Integer id) {
        return singleQueryService.deleteQueryById(id);
    }

    @GetMapping("execute-single-query-by-id/{id}")
    public Mono<ResponseEntity<Void>> executeSingleQueryById(@PathVariable Integer id) {
        return singleQueryService.executeSingleQuery(id);
    }

    @GetMapping("/get-single-query-by-id/{id}")
    public Mono<SingleQueryResponse> getQueryById(@PathVariable Integer id) {
        return singleQueryService.getQueryById(id);
    }

    @GetMapping("/get-all-single-queries")
    public Flux<SingleQueryResponse> getAllSingleQueries() {
        return singleQueryService.getAllQueries();
    }
}