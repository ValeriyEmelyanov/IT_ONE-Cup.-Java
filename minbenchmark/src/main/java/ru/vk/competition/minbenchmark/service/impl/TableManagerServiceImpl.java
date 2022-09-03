package ru.vk.competition.minbenchmark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.vk.competition.minbenchmark.dao.TableManagerDao;
import ru.vk.competition.minbenchmark.exception.InvalidColumnTypeException;
import ru.vk.competition.minbenchmark.exception.TableAlreadyExistsException;
import ru.vk.competition.minbenchmark.exception.TableNotExistsException;
import ru.vk.competition.minbenchmark.exception.TableNotExistsException200;
import ru.vk.competition.minbenchmark.service.TableManagerService;
import ru.vk.competition.minbenchmark.ui.request.TableColumnRequest;
import ru.vk.competition.minbenchmark.ui.request.TableRequest;
import ru.vk.competition.minbenchmark.ui.response.TableColumnResponse;
import ru.vk.competition.minbenchmark.ui.response.TableResponse;
import ru.vk.competition.minbenchmark.util.SqlTypes;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TableManagerServiceImpl implements TableManagerService {

    private final TableManagerDao tableManagerDao;

    @Override
    @Transactional
    public void createTable(TableRequest request) {
        Assert.notNull(request, "CreateTableRequest should not be null");
        Assert.isTrue(request.getColumnsAmount() == request.getColumnInfos().size(),
                "The declared amount of columns is not equal to the actual one");
        final int columnInfosSize = request.getColumnInfos().size();
        Assert.isTrue(columnInfosSize > 0, "ColumnInfos size must contain 1 at least");
        final String pk = request.getPrimaryKey().toUpperCase();
        Assert.isTrue(request.getColumnInfos().stream()
                        .map(c -> c.getTitle().toUpperCase())
                        .anyMatch(c -> c.equals(pk)),
                "Invalid primary key");

        if (tableManagerDao.tableExists(request.getTableName())) {
            String errorMessage = String.format("Table with name %s already exists", request.getTableName());
            log.info(errorMessage);
            throw new TableAlreadyExistsException(errorMessage);
        }

        Optional<String> errType = request.getColumnInfos().stream()
                .map(TableColumnRequest::getType)
                .map(String::toUpperCase)
                .filter(y -> !SqlTypes.isValid(y))
                .findFirst();
        if (errType.isPresent()) {
            String errorMessage = "Invalid collumn type";
            log.info(errorMessage);
            throw new InvalidColumnTypeException(errorMessage);
        }

        log.info("Creating table");
        tableManagerDao.createTable(request);
    }

    @Override
    @Transactional(readOnly = true)
    public TableResponse getTableStructure(String tableName) {
        Assert.notNull(tableName, "TableName should not be null");
        Assert.isTrue(!tableName.isBlank(), "TableName should not be empty");
        Assert.isTrue(tableName.length() <= 50, "TableName length should not be greater than 50");

        if (!tableManagerDao.tableExists(tableName)) {
            String errorMessage = String.format("Table with name %s not exists", tableName);
            log.info(errorMessage);
            throw new TableNotExistsException200(errorMessage);
        }

        log.info("Geting info table: {}", tableName);
        List<TableColumnResponse> columns = tableManagerDao.getTableColumns(tableName);
        List<String> primaryKey = tableManagerDao.getPrimaryKey(tableName);

        return TableResponse.builder()
                .tableName(tableName)
                .columnsAmount(columns.size())
                .primaryKey(String.join(", ", primaryKey))
                .columnInfos(columns)
                .build();
    }

    @Override
    @Transactional
    public void deleteTable(String tableName) {
        Assert.notNull(tableName, "TableName should not be null");
        log.info("Deleting the table: {}", tableName);

        if (!tableManagerDao.tableExists(tableName)) {
            String errorMessage = String.format("Table with name %s not exists", tableName);
            log.info(errorMessage);
            throw new TableNotExistsException(errorMessage);
        }

        tableManagerDao.deleteTable(tableName);
    }
}
