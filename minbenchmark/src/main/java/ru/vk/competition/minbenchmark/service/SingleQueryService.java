package ru.vk.competition.minbenchmark.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.ui.request.SingleQueryRequest;
import ru.vk.competition.minbenchmark.ui.response.SingleQueryResponse;

public interface SingleQueryService {
    Flux<SingleQueryResponse> getAllQueries();

    Mono<SingleQueryResponse> getQueryById(Integer id);

    Mono<ResponseEntity<Void>> deleteQueryById(Integer id);

    Mono<ResponseEntity<Void>> addQueryWithQueryId(SingleQueryRequest request);

    Mono<ResponseEntity<Void>> updateQueryWithQueryId(SingleQueryRequest request);

    Mono<ResponseEntity<Void>> executeSingleQuery(Integer id);
}
