package ru.vk.competition.minbenchmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.vk.competition.minbenchmark.entity.SingleQuery;
import ru.vk.competition.minbenchmark.exception.ExecuteSingleQueryException;
import ru.vk.competition.minbenchmark.exception.SingleQueryNotExistsException;
import ru.vk.competition.minbenchmark.exception.SingleQueryNotExistsException500;
import ru.vk.competition.minbenchmark.repository.SingleQueryRepository;
import ru.vk.competition.minbenchmark.ui.request.SingleQueryRequest;
import ru.vk.competition.minbenchmark.ui.response.SingleQueryResponse;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SingleQueryService {

    private final SingleQueryRepository queryRepository;
    private final JdbcTemplate jdbcTemplate;

    public Flux<SingleQueryResponse> getAllQueries() {
        return Mono.fromCallable(queryRepository::findAll)
                .publishOn(Schedulers.boundedElastic())
                .flatMapIterable(x -> x)
                .map(q -> SingleQueryResponse.builder()
                        .queryId(q.getQueryId())
                        .query(q.getQuery()).build());
    }

    public Mono<SingleQueryResponse> getQueryById(Integer id) {
        Assert.notNull(id, "Id should not be null");

        return Mono.fromCallable(() -> {
                    Optional<SingleQuery> byId = queryRepository.findByQueryId(id);
                    if (byId.isEmpty()) {
                        throw new SingleQueryNotExistsException500(
                                String.format("Cannot find tableQuery by Id %s", id));
                    }
                    SingleQuery q = byId.get();
                    return SingleQueryResponse.builder()
                            .queryId(q.getQueryId())
                            .query(q.getQuery())
                            .build();
                }
        ).publishOn(Schedulers.boundedElastic());
    }

    public Mono<ResponseEntity<Void>> deleteQueryById(Integer id) {
        Assert.notNull(id, "Id should not be null");

        return Mono.fromCallable(() -> {
            try {
                if (queryRepository.findByQueryId(id).map(SingleQuery::getQueryId).isEmpty()) {
                    return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
                } else {
                    queryRepository.deleteByQueryId(id);
                    return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
                }
            } catch (Exception e) {
                return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
            }
        }).publishOn(Schedulers.boundedElastic());
    }

    public Mono<ResponseEntity<Void>> addQueryWithQueryId(SingleQueryRequest request) {
        Assert.notNull(request, "SingleQuery should not be null");

        return Mono.fromCallable(() -> {
            queryRepository.save(new SingleQuery(request.getQueryId(), request.getQuery()));
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }).publishOn(Schedulers.boundedElastic());
    }

    public Mono<ResponseEntity<Void>> updateQueryWithQueryId(SingleQueryRequest request) {
        Assert.notNull(request, "SingleQuery should not be null");

        return Mono.fromCallable(() -> {
            queryRepository.findByQueryId(request.getQueryId())
                    .orElseThrow(() -> new RuntimeException(
                            String.format("Cannot find tableQuery by ID %s", request.getQueryId())
                    ));
            queryRepository.save(new SingleQuery(request.getQueryId(), request.getQuery()));
            return ResponseEntity.<Void>ok(null);
        }).publishOn(Schedulers.boundedElastic());
    }

    public Mono<ResponseEntity<Void>> executeSingleQuery(Integer id) {
        Assert.notNull(id, "Id should not be null");

        Optional<SingleQuery> byId = queryRepository.findByQueryId(id);
        if (byId.isEmpty()) {
            String errorMessage = String.format("The single-query with id %s not exists!", id);
            log.info(errorMessage);
            throw new SingleQueryNotExistsException(errorMessage);
        }

        String sql = byId.get().getQuery();
        log.info("SQL to execute: {}", sql);

        return Mono.fromCallable(() -> {
            try {
                jdbcTemplate.execute(sql);
            } catch (Exception e) {
                String errMessage = "Failed to execute the single-query";
                log.error("{}: {}", errMessage, e.getMessage());
                throw new ExecuteSingleQueryException(errMessage, e);
            }
            log.info("SQL done!");
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }).publishOn(Schedulers.boundedElastic());
    }
}