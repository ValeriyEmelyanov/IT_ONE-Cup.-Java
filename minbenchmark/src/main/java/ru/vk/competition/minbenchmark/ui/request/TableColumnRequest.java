package ru.vk.competition.minbenchmark.ui.request;

import lombok.Data;
import ru.vk.competition.minbenchmark.util.ConstraintsUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class TableColumnRequest {

    @NotBlank
    @Pattern(regexp = ConstraintsUtil.IDENTIFIER_PATTERN)
    private final String title;

    @NotBlank
    private final String type;

}
