package ru.vk.competition.minbenchmark.ui.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import ru.vk.competition.minbenchmark.serializer.ToUpperCaseConverter;

@Data
@Builder
public class TableColumnResponse {

    @JsonSerialize(converter = ToUpperCaseConverter.class)
    private final String title;

    @JsonSerialize(converter = ToUpperCaseConverter.class)
    private final String type;
}
