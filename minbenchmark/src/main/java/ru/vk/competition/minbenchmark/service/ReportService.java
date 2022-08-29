package ru.vk.competition.minbenchmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.vk.competition.minbenchmark.dao.TableManagerDao;
import ru.vk.competition.minbenchmark.entity.Report;
import ru.vk.competition.minbenchmark.entity.ReportTable;
import ru.vk.competition.minbenchmark.entity.ReportTableColumn;
import ru.vk.competition.minbenchmark.exception.ColumnNotExistsException;
import ru.vk.competition.minbenchmark.exception.InvalidColumnTypeException;
import ru.vk.competition.minbenchmark.exception.RepodtAlreadyExistsException;
import ru.vk.competition.minbenchmark.exception.ReportNotExistsException;
import ru.vk.competition.minbenchmark.exception.TableNotExistsException;
import ru.vk.competition.minbenchmark.repository.ReportRepository;
import ru.vk.competition.minbenchmark.ui.request.ReportRequest;
import ru.vk.competition.minbenchmark.ui.request.ReportTableRequest;
import ru.vk.competition.minbenchmark.ui.request.TableColumnRequest;
import ru.vk.competition.minbenchmark.ui.response.ReportResponse;
import ru.vk.competition.minbenchmark.ui.response.ReportTableColumnResponse;
import ru.vk.competition.minbenchmark.ui.response.ReportTableResponse;
import ru.vk.competition.minbenchmark.util.SqlTypes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final TableManagerDao tableManagerDao;

    @Transactional(readOnly = true)
    public Mono<ReportResponse> getQueryById(Integer id) {
        Assert.notNull(id, "Id should not be null");

        return Mono.fromCallable(
                () -> {
                    Report report = reportRepository.findByReportId(id).orElseThrow(
                            () -> new ReportNotExistsException(
                                    String.format("Cannot find tableQuery by Id %s", id.toString())
                            ));
                    return ReportResponse.builder()
                            .reportId(report.getReportId())
                            .tableAmount(report.getTables().size())
                            .tables(report.getTables().stream()
                                    .map(t -> ReportTableResponse.builder()
                                            .tableName(t.getTableName())
                                            .columns(t.getColumns().stream()
                                                    .map(c -> new ReportTableColumnResponse(c.getTitle(),
                                                            c.getType(),
                                                            "10"))
                                                    .collect(Collectors.toList()))
                                            .build())
                                    .collect(Collectors.toList()))
                            .build();
                }
        ).publishOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<ResponseEntity<Void>> createReport(ReportRequest request) {
        Assert.notNull(request, "ReportRequest should not be null");
        Assert.isTrue(request.getTableAmount() == request.getTables().size(),
                "The table amount must be equal table size");

        if (reportRepository.existsById(request.getReportId())) {
            String errorMessage = String.format("The report with id %s already exists!", request.getReportId());
            log.info(errorMessage);
            throw new RepodtAlreadyExistsException(errorMessage);
        }

        if (!tableManagerDao.tablesExist(request.getTables().stream()
                .map(ReportTableRequest::getTableName)
                .collect(Collectors.toList()))) {
            String errorMessage = "Not all tables exist!";
            log.info(errorMessage);
            throw new TableNotExistsException(errorMessage);
        }

//        request.getTables().forEach(t -> {
//            List<String> columns = t.getColumns().stream()
//                    .map(TableColumnRequest::getTitle)
//                    .collect(Collectors.toList());
//            if (!tableManagerDao.tableColumnsExist(t.getTableName(), columns)) {
//                String errorMessage = "Not all table columns exist!";
//                log.info(errorMessage);
//                throw new ColumnNotExistsException(errorMessage);
//            }
//        });

        Optional<String> errType = request.getTables().stream()
                .flatMap(t -> t.getColumns().stream())
                .map(TableColumnRequest::getType)
                .map(String::toUpperCase)
                .filter(y -> !SqlTypes.isValid(y))
                .findFirst();
        if (errType.isPresent()) {
            String errorMessage = "Invalid collumn type";
            log.info(errorMessage);
            throw new InvalidColumnTypeException(errorMessage);
        }

        return Mono.fromCallable(() -> {
            Report report = Report.builder()
                    .reportId(request.getReportId())
                    .tables(request.getTables().stream()
                            .map(t -> ReportTable.builder()
                                    .tableName(t.getTableName())
                                    .columns(t.getColumns().stream()
                                            .map(c -> ReportTableColumn.builder()
                                                    .title(c.getTitle())
                                                    .type(c.getType())
                                                    .build())
                                            .collect(Collectors.toList()))
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
            report.getTables().forEach(t -> {
                t.setReport(report);
                t.getColumns().forEach(c -> c.setTable(t));
            });

            reportRepository.save(report);

            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }).publishOn(Schedulers.boundedElastic());
    }
}
