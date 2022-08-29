package ru.vk.competition.minbenchmark.ui.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ReportRequest {

    @NotNull
    private final Integer reportId;

    @NotNull
    @Min(1)
    private final Integer tableAmount;

    @NotNull
    private final List<ReportTableRequest> tables;
}
