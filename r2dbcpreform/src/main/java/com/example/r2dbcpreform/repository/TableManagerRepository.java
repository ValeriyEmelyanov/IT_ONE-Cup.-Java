package com.example.r2dbcpreform.repository;

import com.example.r2dbcpreform.exception.BuildCreateTableSqlException;
import com.example.r2dbcpreform.exception.CreateTableException;
import com.example.r2dbcpreform.exception.DeleteTableException;
import com.example.r2dbcpreform.ui.request.TableColumnRequest;
import com.example.r2dbcpreform.ui.request.TableRequest;
import com.example.r2dbcpreform.ui.response.TableColumnResponse;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

import static java.lang.String.format;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TableManagerRepository {
    private final ConnectionFactory connectionFactory;

    public Mono<Void> createTable(TableRequest request) {
        return DatabaseClient.create(connectionFactory)
                .sql(() -> {
                    String sql = buildCreateTableSql(request);
                    log.info("SQL to execute: {}", sql);
                    return sql;
                })
                .then()
                .doOnError(e -> {
                    String errMessage = "Failed to execute the sql";
                    log.error("{}: {}", errMessage, e.getMessage());
                    throw new CreateTableException(errMessage, e);
                });
    }

    private String buildCreateTableSql(TableRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            String primaryKey = request.getPrimaryKey();
            sb.append("CREATE TABLE ").append(request.getTableName()).append(" (");
            for (TableColumnRequest field : request.getColumnInfos()) {
                sb.append(field.getTitle()).append(" ").append(field.getType());
                if (field.getTitle().equals(primaryKey)) {
                    sb.append(" NOT NULL");
                }
                sb.append(", ");
            }
            sb.append("PRIMARY KEY (").append(primaryKey).append(")");
            sb.append(");");
        } catch (Exception e) {
            String errMessage = "Failed to build the sql text";
            log.error("{}: {}", errMessage, e.getMessage());
            throw new BuildCreateTableSqlException(errMessage, e);
        }
        return sb.toString();
    }

    public Mono<Boolean> tableExists(String tableName) {
        final String sql = "SELECT TRUE AS F FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME=:tablename;";
        return DatabaseClient.create(connectionFactory).sql(sql)
                .bind("tablename", tableName.toUpperCase())
                .map((row, rowMataData) -> row.get("F", Boolean.class))
                .first()
                .defaultIfEmpty(Boolean.FALSE);
    }

    public Flux<TableColumnResponse> getTableColumns(String tableName) {
        final String sql = "SELECT COLUMN_NAME AS title, DATA_TYPE AS type FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME=:tablename;";
        final BiFunction<Row, RowMetadata, TableColumnResponse> mapping =
                (row, rowMetadata) -> TableColumnResponse.builder()
                        .title(row.get("title", String.class))
                        .type(row.get("type", String.class))
                        .build();
        return DatabaseClient.create(connectionFactory).sql(sql)
                .bind("tablename", tableName.toUpperCase())
                .map(mapping)
                .all();
    }

    public Flux<String> getPrimaryKey(String tableName) {
        final String sql = "SELECT T2.COLUMN_NAME FROM " +
                "(SELECT INDEX_NAME FROM INFORMATION_SCHEMA.INDEXES " +
                "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME=:tablename AND INDEX_TYPE_NAME='PRIMARY KEY') AS T1 " +
                "INNER JOIN " +
                "(SELECT INDEX_NAME, COLUMN_NAME FROM INFORMATION_SCHEMA.INDEX_COLUMNS " +
                "WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME=:tablename) AS T2 " +
                "ON T1.INDEX_NAME = T2.INDEX_NAME;";
        return DatabaseClient.create(connectionFactory).sql(sql)
                .bind("tablename", tableName.toUpperCase())
                .map((row, rowMatadata) -> row.get("column_name", String.class))
                .all();
    }

    public Mono<Void> deleteTable(String tableName) {
        final String sql = format("DROP TABLE %s;", tableName);
        return DatabaseClient.create(connectionFactory)
                .sql(sql)
                .then()
                .doOnError(e -> {
                    String errMessage = format("Failed to delete the table %s", tableName);
                    log.error("{}: {}", errMessage, e.getMessage());
                    throw new DeleteTableException(errMessage, e);
                });
    }
}
