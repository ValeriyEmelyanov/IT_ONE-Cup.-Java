package ru.vk.competition.minbenchmark.service;

import ru.vk.competition.minbenchmark.ui.request.TableRequest;
import ru.vk.competition.minbenchmark.ui.response.TableResponse;

public interface TableManagerService {
    void createTable(TableRequest request);

    TableResponse getTableStructure(String tableName);

    void deleteTable(String tableName);
}
