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
    public ResponseEntity<PortCongestionData> save(@RequestBody PortCongestionData data) {
        log.info("API: Saving PortCongestionData {}", data);

        PortCongestionData saved = service.save(data, "UTC");
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortCongestionData> findById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PortCongestionData>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/situation/{type}")
    public ResponseEntity<List<PortCongestionData>> findBySituationType(@PathVariable SituationType type) {
        return ResponseEntity.ok(service.findBySituationType(type));
    }

    @GetMapping("/count/{type}")
    public ResponseEntity<Integer> countBySituationType(@PathVariable SituationType type) {
        return ResponseEntity.ok(service.countBySituationType(type));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
