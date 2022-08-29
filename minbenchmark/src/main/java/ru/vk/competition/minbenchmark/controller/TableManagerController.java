package ru.vk.competition.minbenchmark.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.competition.minbenchmark.service.TableManagerService;
import ru.vk.competition.minbenchmark.ui.request.TableRequest;
import ru.vk.competition.minbenchmark.ui.response.TableResponse;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/table")
@RequiredArgsConstructor
@Validated
public class TableManagerController {

    private final TableManagerService tableManagerService;

    @GetMapping("/test")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/create-table")
    public ResponseEntity<Void> createTable(@Valid @RequestBody TableRequest request) {
        tableManagerService.createTable(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get-table-by-name/{name}")
    public ResponseEntity<TableResponse> getTableStructure(@PathVariable String name) {
        return ResponseEntity.ok(tableManagerService.getTableStructure(name));
    }

    @DeleteMapping({"/drop-table-by-name/{name}", "/drop-table/{name}"})
    public ResponseEntity<Void> deleteTable(@PathVariable String name) {
        tableManagerService.deleteTable(name);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
