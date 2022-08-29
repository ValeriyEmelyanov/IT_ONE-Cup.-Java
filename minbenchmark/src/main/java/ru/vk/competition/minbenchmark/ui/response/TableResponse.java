package ru.vk.competition.minbenchmark.ui.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import ru.vk.competition.minbenchmark.serializer.ToLowerCaseConverter;

import java.util.List;

@Data
@Builder
public class TableResponse {

    private final String tableName;

    private final int columnsAmount;

    @JsonSerialize(converter = ToLowerCaseConverter.class)
    private final String primaryKey;

    private final List<TableColumnResponse> columnInfos;
}
