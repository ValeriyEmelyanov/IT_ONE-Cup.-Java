package ru.vk.competition.minbenchmark.ui.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TableQueryResponse {

    private final Integer queryId;

    private final String tableName;

    private final String query;

}
