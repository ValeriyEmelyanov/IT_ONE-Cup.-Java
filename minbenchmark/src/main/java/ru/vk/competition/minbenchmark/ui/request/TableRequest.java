package ru.vk.competition.minbenchmark.ui.request;

import lombok.Data;
import ru.vk.competition.minbenchmark.util.ConstraintsUtil;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class TableRequest {

    @NotBlank
    @Size(min = 1, max = ConstraintsUtil.TABLE_NAME_MAX_SIZE)
    @Pattern(regexp = ConstraintsUtil.IDENTIFIER_PATTERN)
    private final String tableName;

    @Min(1)
    private final int columnsAmount;

    @NotBlank
    @Pattern(regexp = ConstraintsUtil.IDENTIFIER_PATTERN)
    private final String primaryKey;

    @NotNull
    private final List<TableColumnRequest> columnInfos;

}
