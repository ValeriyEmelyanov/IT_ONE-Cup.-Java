package ru.vk.competition.minbenchmark.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vk.competition.minbenchmark.exception.BuildCreateTableSqlException;
import ru.vk.competition.minbenchmark.exception.CreateTableException;
import ru.vk.competition.minbenchmark.exception.DeleteTableException;
import ru.vk.competition.minbenchmark.exception.GetPrimaryKeyException;
import ru.vk.competition.minbenchmark.exception.GetTableColumnsExceprion;
import ru.vk.competition.minbenchmark.ui.request.TableColumnRequest;
import ru.vk.competition.minbenchmark.ui.request.TableRequest;
import ru.vk.competition.minbenchmark.ui.response.TableColumnResponse;

import java.util.List;

import static java.lang.String.format;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TableManagerDao {

    private final JdbcTemplate jdbcTemplate;

    public boolean tableExists(String tableName) {
        try {
            final String sql = "SELECT 1 AS F FROM INFORMATION_SCHEMA.TABLES " +
                    "WHERE TABLE_SCHEMA=? AND TABLE_NAME=?;";
            List<Integer> result = jdbcTemplate.query(sql,
                    (rs, rnum) -> rs.getInt("f"),
                    "PUBLIC", tableName.toUpperCase());
            return !result.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void createTable(TableRequest request) {
        String sql = buildCreateTableSql(request);
        log.info("SQL to execute: {}", sql);
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            String errMessage = "Failed to execute the sql";
            log.error("{}: {}", errMessage, e.getMessage());
            throw new CreateTableException(errMessage, e);
        }
        log.info("SQL done!");
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

    public List<TableColumnResponse> getTableColumns(String tableName) {
        final String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA=? AND TABLE_NAME=?;";
        try {
            return jdbcTemplate.query(sql,
                    (rs, rnum) -> TableColumnResponse.builder()
                            .title(rs.getString("column_name"))
                            .type(rs.getString("data_type"))
                            .build(),
                    "PUBLIC", tableName.toUpperCase());
        } catch (DataAccessException e) {
            String errorMessage = format("Failed to get a description of the table columns %s", tableName);
            log.error(errorMessage);
            throw new GetTableColumnsExceprion(errorMessage);
        }
    }

    public List<String> getPrimaryKey(String tableName) {
        final String sql = "SELECT T2.COLUMN_NAME FROM " +
                "(SELECT INDEX_NAME FROM INFORMATION_SCHEMA.INDEXES " +
                "WHERE TABLE_SCHEMA=? AND TABLE_NAME=? AND INDEX_TYPE_NAME='PRIMARY KEY') AS T1 " +
                "INNER JOIN " +
                "(SELECT INDEX_NAME, COLUMN_NAME FROM INFORMATION_SCHEMA.INDEX_COLUMNS " +
                "WHERE TABLE_SCHEMA=? AND TABLE_NAME=?) AS T2 " +
                "ON T1.INDEX_NAME = T2.INDEX_NAME;";
        try {
            return jdbcTemplate.query(sql,
                    (rs, rnum) -> rs.getString("column_name"),
                    "PUBLIC", tableName.toUpperCase(),
                    "PUBLIC", tableName.toUpperCase());
        } catch (DataAccessException e) {
            String errorMessage = format("Failed to get a primary key description of the table %s", tableName);
            log.error(errorMessage);
            throw new GetPrimaryKeyException(errorMessage);
        }
    }

    public void deleteTable(String tableName) {
        final String sql = format("DROP TABLE %s;", tableName);
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            String errMessage = format("Failed to delete the table ", tableName);
            log.error("{}: {}", errMessage, e.getMessage());
            throw new DeleteTableException(errMessage, e);
        }
        log.info("Table {} deleted!", tableName);
    }

    public boolean tablesExist(List<String> names) {
        StringBuilder sb = new StringBuilder("SELECT TABLE_NAME AS F FROM INFORMATION_SCHEMA.TABLES ");
        sb.append("WHERE TABLE_SCHEMA='PUBLIC' AND (FALSE");
        String template = " OR TABLE_NAME='%s'";
        names.forEach(n -> sb.append(String.format(template, n.toUpperCase())));
        sb.append(");");
        final String sql = sb.toString();
        try {
            List<String> result = jdbcTemplate.query(sql,
                    (rs, rnum) -> rs.getString("f"));
            return result.size() == names.size();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean tableColumnsExist(String tableName, List<String> columns) {
        StringBuilder sb = new StringBuilder("SELECT COLUMN_NAME AS F FROM INFORMATION_SCHEMA.COLUMNS ");
        sb.append(String.format("WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='%s' AND (FALSE", tableName));
        String template = " OR COLUMN_NAME='%s'";
        columns.forEach(c -> sb.append(String.format(template, c)));
        sb.append(");");
        final String sql = sb.toString();
        try {
            List<String> result = jdbcTemplate.query(sql,
                    (rs, rnum) -> rs.getString("f"));
            return result.size() == columns.size();
        } catch (Exception e) {
            return false;
        }
    }
}
