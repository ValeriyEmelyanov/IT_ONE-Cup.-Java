package ru.vk.competition.minbenchmark.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.vk.competition.minbenchmark.service.ReportService;
import ru.vk.competition.minbenchmark.ui.request.ReportRequest;
import ru.vk.competition.minbenchmark.ui.response.ReportResponse;

@Slf4j
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/get-report-by-id/{id}")
    public Mono<ReportResponse> getQueryById(@PathVariable Integer id) {
        return reportService.getQueryById(id);
    }

    @PostMapping("/create-report")
    public Mono<ResponseEntity<Void>> createReport(@RequestBody ReportRequest request) {
        return reportService.createReport(request);
    }
}
