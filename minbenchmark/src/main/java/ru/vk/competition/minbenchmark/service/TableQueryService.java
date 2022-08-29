package ru.vk.competition.minbenchmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.vk.competition.minbenchmark.dao.TableManagerDao;
import ru.vk.competition.minbenchmark.entity.TableQuery;
import ru.vk.competition.minbenchmark.exception.CreateTableQueryException;
import ru.vk.competition.minbenchmark.exception.ExecuteTableQueryException;
import ru.vk.competition.minbenchmark.exception.ModifyTableQueryException;
import ru.vk.competition.minbenchmark.exception.TableNotExistsException;
import ru.vk.competition.minbenchmark.exception.TableQueryNotExistsException;
import ru.vk.competition.minbenchmark.exception.TableQueryNotExistsException500;
import ru.vk.competition.minbenchmark.repository.TableQueryRepository;
import ru.vk.competition.minbenchmark.ui.request.TableQueryRequest;
import ru.vk.competition.minbenchmark.ui.response.TableQueryResponse;
import ru.vk.competition.minbenchmark.util.ConstraintsUtil;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TableQueryService {

    private final TableQueryRepository tableQueryRepository;
    private final TableManagerDao tableManagerDao;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Mono<ResponseEntity<Void>> addQueryWithQueryId(TableQueryRequest request) {
        Assert.notNull(request, "Query request should not be null");

        log.info("Adding table query request ...");

        if (!tableManagerDao.tableExists(request.getTableName())) {
            String errorMessage = String.format("The table %s not exists!", request.getTableName());
            log.info(errorMessage);
            throw new TableNotExistsException(errorMessage);
        }

        try {
            return Mono.fromCallable(() -> {
                tableQueryRepository.save(
                        new TableQuery(request.getQueryId(),
                                request.getTableName(),
                                request.getQuery())
                );
                return new ResponseEntity<Void>(HttpStatus.CREATED);
            }).publishOn(Schedulers.boundedElastic());
        } catch (Exception e) {
            String errorMessage = "Failed to add a new request to a table";
            log.error("{}: {}", errorMessage, e.getMessage());
            throw new CreateTableQueryException(errorMessage, e);
        }
    }

    @Transactional
    public Mono<ResponseEntity<Void>> modifyQueryInTable(TableQueryRequest request) {
        Assert.notNull(request, "Query request should not be null");

        if (!tableManagerDao.tableExists(request.getTableName())) {
            String errorMessage = String.format("The table %s not exists!", request.getTableName());
            log.info(errorMessage);
            throw new TableNotExistsException(errorMessage);
        }

        log.info("Modifying table query request ...");

        if (!tableQueryRepository.existsById(request.getQueryId())) {
            String errorMessage = String.format("The table query with id %s not exists!", request.getQueryId());
            log.info(errorMessage);
            throw new TableQueryNotExistsException(errorMessage);
        }


        try {
            return Mono.fromCallable(() -> {
                tableQueryRepository.save(
                        new TableQuery(request.getQueryId(),
                                request.getTableName(),
                                request.getQuery())
                );
                return new ResponseEntity<Void>(HttpStatus.OK);
            }).publishOn(Schedulers.boundedElastic());
        } catch (Exception e) {
            String errorMessage = "Failed to modify the request to a table";
            log.error("{}: {}", errorMessage, e.getMessage().substring(0, 100));
            throw new ModifyTableQueryException(errorMessage, e);
        }
    }

    @Transactional
    public Mono<ResponseEntity<Void>> deleteTableQueryById(Integer id) {
        Assert.notNull(id, "Id should not be null");

        log.info("Deleting table query by id ...");

        if (!tableQueryRepository.existsById(id)) {
            String errorMessage = String.format("The table query with id %s not exists!", id);
            log.info(errorMessage);
            throw new TableQueryNotExistsException(errorMessage);
        }

        try {
            return Mono.fromCallable(() -> {
                tableQueryRepository.deleteById(id);
                return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
            }).publishOn(Schedulers.boundedElastic());
        } catch (Exception e) {
            String errorMessage = "Failed to delete the table query by id";
            log.error("{}: {}", errorMessage, e.getMessage());
            throw e;
        }
    }

    @Transactional
    public Mono<ResponseEntity<Void>> executeTableQueryById(Integer id) {
        Assert.notNull(id, "Id should not be null");

        log.info("Executing table query by id ...");

        Optional<TableQuery> byId = tableQueryRepository.findById(id);
        if (byId.isEmpty()) {
            String errorMessage = String.format("The table query with id %s not exists!", id);
            log.info(errorMessage);
            throw new TableQueryNotExistsException(errorMessage);
        }

        String sql = byId.get().getQuery();
        log.info("SQL to execute: {}", sql);

        return Mono.fromCallable(() -> {
            try {
                jdbcTemplate.execute(sql);
            } catch (Exception e) {
                String errMessage = "Failed to execute the sql";
                log.error("{}: {}", errMessage, e.getMessage());
                throw new ExecuteTableQueryException(errMessage, e);
            }
            log.info("SQL done!");
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }).publishOn(Schedulers.boundedElastic());
    }

    @Transactional(readOnly = true)
    public Flux<TableQueryResponse> getAllQueriesByTableName(String name) {
        Assert.notNull(name, "The table name should not be null");

        log.info("Getting all table query by table name ...");

        return Mono.fromCallable(() -> tableQueryRepository.findAllByTableName(name))
                .publishOn(Schedulers.boundedElastic())
                .flatMapIterable(x -> x)
                .map(
                        q -> TableQueryResponse.builder()
                                .queryId(q.getQueryId())
                                .tableName(q.getTableName())
                                .query(q.getQuery())
                                .build()
                );
    }

    @Transactional(readOnly = true)
    public Mono<TableQueryResponse> getTableQueryById(Integer id) {
        Assert.notNull(id, "Id should not be null");

        log.info("Getting table query by id ...");

        return Mono.fromCallable(() -> {
                    Optional<TableQuery> byId = tableQueryRepository.findByQueryId(id);
                    if (byId.isEmpty()) {
                        throw new TableQueryNotExistsException500(
                                String.format("Cannot find tableQuery by Id %s", id));
                    }
                    TableQuery q = byId.get();
                    return TableQueryResponse.builder()
                            .queryId(q.getQueryId())
                            .tableName(q.getQuery())
                            .query(q.getQuery())
                            .build();
                }
        ).publishOn(Schedulers.boundedElastic());
    }

    @Transactional(readOnly = true)
    public Flux<TableQueryResponse> getAllTableQueries() {
        log.info("Getting all table queries ...");

        return Mono.fromCallable(tableQueryRepository::findAll)
                .publishOn(Schedulers.boundedElastic())
                .flatMapIterable(x -> x)
                .map(
                        q -> TableQueryResponse.builder()
                                .queryId(q.getQueryId())
                                .tableName(q.getTableName())
                                .query(q.getQuery())
                                .build()
                );
    }
}
