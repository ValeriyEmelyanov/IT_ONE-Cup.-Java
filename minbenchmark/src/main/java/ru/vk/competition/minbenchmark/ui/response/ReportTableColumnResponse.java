package ru.vk.competition.minbenchmark.ui.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReportTableColumnResponse {
    private final String title;
    private final String type;
    private final String size;
}
