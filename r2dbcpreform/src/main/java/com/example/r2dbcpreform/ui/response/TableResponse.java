package com.example.r2dbcpreform.ui.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TableResponse {

    private final String tableName;

    private final int columnsAmount;

    private final String primaryKey;

    private final List<TableColumnResponse> columnInfos;
}
