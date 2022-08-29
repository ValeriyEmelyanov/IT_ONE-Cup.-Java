package ru.vk.competition.minbenchmark.ui.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class ReportTableRequest {

    @NotBlank
//    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_]+")
    private final String tableName;

    @NotNull
    private final List<TableColumnRequest> columns;

}
