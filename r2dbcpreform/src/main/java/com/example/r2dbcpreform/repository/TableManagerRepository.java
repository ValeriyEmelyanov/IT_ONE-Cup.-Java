package com.example.r2dbcpreform.repository;

import com.example.r2dbcpreform.exception.BuildCreateTableSqlException;
import com.example.r2dbcpreform.exception.CreateTableException;
import com.example.r2dbcpreform.ui.request.TableColumnRequest;
import com.example.r2dbcpreform.ui.request.TableRequest;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

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
}
