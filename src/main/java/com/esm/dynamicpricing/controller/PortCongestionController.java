package com.esm.dynamicpricing.controller;

import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;
import com.esm.dynamicpricing.service.PortCongestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/port-congestion")
@RequiredArgsConstructor
@Slf4j
public class PortCongestionController {

    private final PortCongestionService service;

@PostMapping
public ResponseEntity<PortCongestionData> save(
        @RequestBody PortCongestionData data,
        @RequestHeader(name = "X-User-Timezone", required = false) String userTimezone) {
    
    log.info("API: Saving PortCongestionData {}", data);

    // Default to UTC if not provided
    if (userTimezone == null || userTimezone.isBlank()) {
        userTimezone = "UTC";
    }

    PortCongestionData saved = service.save(data, userTimezone);
    return ResponseEntity.ok(saved);
}


    @GetMapping("/{id}")
    public ResponseEntity<PortCongestionData> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Find All
    @GetMapping
    public ResponseEntity<List<PortCongestionData>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // ✅ Find by SituationType (enum)
    @GetMapping("/situation/{type}")
    public ResponseEntity<List<PortCongestionData>> findBySituationType(@PathVariable SituationType type) {
        return ResponseEntity.ok(service.findBySituationType(type));
    }

    // ✅ Count by SituationType
    @GetMapping("/count/{type}")
    public ResponseEntity<Integer> countBySituationType(@PathVariable SituationType type) {
        return ResponseEntity.ok(service.countBySituationType(type));
    }

    // ✅ Exists by ID
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    // ✅ Delete by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

