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
import ru.vk.competition.minbenchmark.service.TableQueryService;
import ru.vk.competition.minbenchmark.ui.request.TableQueryRequest;
import ru.vk.competition.minbenchmark.ui.response.TableQueryResponse;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/table-query")
@RequiredArgsConstructor
@Validated
public class TableQueryController {

    private final TableQueryService tableQueryService;

    @PostMapping("/add-new-query-to-table")
    public Mono<ResponseEntity<Void>> addNewQueryToTable(@Valid @RequestBody TableQueryRequest request) {
        return tableQueryService.addQueryWithQueryId(request);
    }

    @PutMapping("/modify-query-in-table")
    public Mono<ResponseEntity<Void>> modifyQueryInTable(@Valid @RequestBody TableQueryRequest request) {
        return tableQueryService.modifyQueryInTable(request);
    }

    @DeleteMapping("/delete-table-query-by-id/{id}")
    public Mono<ResponseEntity<Void>> deleteTableQueryById(@PathVariable Integer id) {
        return tableQueryService.deleteTableQueryById(id);
    }

    @GetMapping("/execute-table-query-by-id/{id}")
    public Mono<ResponseEntity<Void>> executeTableQueryById(@PathVariable Integer id) {
        return tableQueryService.executeTableQueryById(id);
    }

    @GetMapping("/get-all-queries-by-table-name/{name}")
    public Flux<TableQueryResponse> getAllQueriesByTableName(@PathVariable String name) {
        return tableQueryService.getAllQueriesByTableName(name);
    }

    @GetMapping("/get-table-query-by-id/{id}")
    public Mono<TableQueryResponse> getTableQueryById(@PathVariable Integer id) {
        return tableQueryService.getTableQueryById(id);
    }

    @GetMapping("/get-all-table-queries")
    public Flux<TableQueryResponse> getAllTableQueries() {
        return tableQueryService.getAllTableQueries();
    }
}
