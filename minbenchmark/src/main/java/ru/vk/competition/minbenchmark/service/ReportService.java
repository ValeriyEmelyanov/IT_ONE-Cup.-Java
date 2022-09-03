package ru.vk.competition.minbenchmark.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.ui.request.ReportRequest;
import ru.vk.competition.minbenchmark.ui.response.ReportResponse;

public interface ReportService {
    Mono<ReportResponse> getQueryById(Integer id);

    Mono<ResponseEntity<Void>> createReport(ReportRequest request);
}
