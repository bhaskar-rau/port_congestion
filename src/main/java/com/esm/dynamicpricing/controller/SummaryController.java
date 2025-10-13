package com.esm.dynamicpricing.controller;

import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService service;

    public SummaryController(SummaryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getSummary(
            @RequestParam(required = false) String port,
            @RequestParam(required = false) String terminal,
            @RequestParam(required = false) LocalDate minDate,
            @RequestParam(required = false) LocalDate maxDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<PortCongestionData> records = service.getSummary(port, terminal, minDate, maxDate, page, size);
        int totalRecords = service.countSummary(port, terminal, minDate, maxDate);

        Map<String, Object> response = new HashMap<>(); 
        response.put("page", page);
        response.put("size", size);
        response.put("totalRecords", totalRecords);
        response.put("records", records);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortCongestionData> updateRecord(
            @PathVariable Integer id,
            @RequestBody PortCongestionData updated) {
        return ResponseEntity.ok(service.updateRecord(id, updated));
    }

    

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecord(@PathVariable Integer id) {
        service.deleteRecord(id);
        return ResponseEntity.ok("Record deleted successfully with id " + id);
    }
}
