package ru.vk.competition.minbenchmark.ui.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class TableRequest {

    @NotBlank
//    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_$#@]+")
//    @Size(max = 128)
    private final String tableName;

    @Min(1)
    private final int columnsAmount;

    @NotBlank
    private final String primaryKey;

    @NotNull
    private final List<TableColumnRequest> columnInfos;

}
