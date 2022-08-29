package ru.vk.competition.minbenchmark.ui.response;

import lombok.Builder;
import lombok.Data;
import ru.vk.competition.minbenchmark.ui.request.TableColumnRequest;

import java.util.List;

@Data
@Builder
public class ReportTableResponse {
    private final String tableName;
    private final List<ReportTableColumnResponse> columns;
}
