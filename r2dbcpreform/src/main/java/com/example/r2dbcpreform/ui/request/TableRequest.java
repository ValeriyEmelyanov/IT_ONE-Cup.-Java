package com.example.r2dbcpreform.ui.request;

import lombok.Data;

import java.util.List;

@Data
public class TableRequest {

    private final String tableName;

    private final int columnsAmount;

    private final String primaryKey;

    private final List<TableColumnRequest> columnInfos;

}
