package ru.vk.competition.minbenchmark.ui.response;

import lombok.Builder;
import lombok.Data;
import ru.vk.competition.minbenchmark.ui.request.ReportTableRequest;

import java.util.List;

@Data
@Builder
public class ReportResponse {
    private final Integer reportId;
    private final Integer tableAmount;
    private final List<ReportTableResponse> tables;
}
