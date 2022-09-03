package ru.vk.competition.minbenchmark.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.ui.request.TableQueryRequest;
import ru.vk.competition.minbenchmark.ui.response.TableQueryResponse;

public interface TableQueryService {
    Mono<ResponseEntity<Void>> addQueryWithQueryId(TableQueryRequest request);

    Mono<ResponseEntity<Void>> modifyQueryInTable(TableQueryRequest request);

    Mono<ResponseEntity<Void>> deleteTableQueryById(Integer id);

    Mono<ResponseEntity<Void>> executeTableQueryById(Integer id);

    Flux<TableQueryResponse> getAllQueriesByTableName(String name);

    Mono<TableQueryResponse> getTableQueryById(Integer id);

    Flux<TableQueryResponse> getAllTableQueries();
}
