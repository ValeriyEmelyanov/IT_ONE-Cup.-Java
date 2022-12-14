package com.example.r2dbcpreform.controller;

import com.example.r2dbcpreform.exception.CreateTableException;
import com.example.r2dbcpreform.exception.TableNotExistsException;
import com.example.r2dbcpreform.repository.TableManagerRepository;
import com.example.r2dbcpreform.ui.request.TableRequest;
import com.example.r2dbcpreform.ui.response.TableColumnResponse;
import com.example.r2dbcpreform.ui.response.TableResponse;
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
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableManagerController {
    private final TableManagerRepository tableManagerRepository;

    @PostMapping
    public Mono<ResponseEntity<Void>> createTable(@RequestBody TableRequest request) {
        return tableManagerRepository.tableExists(request.getTableName())
                .flatMap(it -> !it ?
                        tableManagerRepository.createTable(request)
                                .then(Mono.just(new ResponseEntity<>(HttpStatus.CREATED))) :
                        Mono.error(new CreateTableException("The table " + request.getTableName() +
                                " already exists."))
                );
    }

    @GetMapping("/{tablename}/exists")
    public Mono<Boolean> tableExists(@PathVariable("tablename") String tableName) {
        return tableManagerRepository.tableExists(tableName);
    }

    @GetMapping("/{tablename}/columns")
    public Flux<TableColumnResponse> getTableColumns(@PathVariable("tablename") String tableName) {
        return tableManagerRepository.getTableColumns(tableName);
    }

    @GetMapping("/{tablename}/primarykey")
    public Flux<String> getPrimaryKey(@PathVariable("tablename") String tableName) {
        return tableManagerRepository.getPrimaryKey(tableName);
    }

    @GetMapping("/{tablename}")
    public Mono<TableResponse> getTableStructure(@PathVariable("tablename") String tableName) {
        return tableManagerRepository.tableExists(tableName)
                .flatMap(it -> it ?
                        Mono.zip(
                                        tableManagerRepository.getTableColumns(tableName).collectList(),
                                        tableManagerRepository.getPrimaryKey(tableName).collectList()
                                )
                                .map(t -> TableResponse.builder()
                                        .tableName(tableName)
                                        .columnsAmount(t.getT1().size())
                                        .primaryKey(String.join(",", t.getT2()))
                                        .columnInfos(t.getT1())
                                        .build()) :
                        Mono.error(new TableNotExistsException("The table " + tableName +
                                " does not exist."))
                );
    }

    @DeleteMapping("/{tablename}")
    public Mono<ResponseEntity<Void>> deleteTable(@PathVariable("tablename") String tableName) {
        return tableManagerRepository.tableExists(tableName)
                .flatMap(it -> it ?
                        tableManagerRepository.deleteTable(tableName)
                                .then(Mono.just(new ResponseEntity<>(HttpStatus.ACCEPTED))):
                        Mono.error(new TableNotExistsException("The table " + tableName +
                                " does not exist."))
                );
    }
}
