package com.esm.dynamicpricing.service;

import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;

import java.util.List;
import java.util.Optional;

public interface PortCongestionService {

    // Create or update a record with user timezone for datetime fields
    PortCongestionData save(PortCongestionData data, String userTimezone);

    // Find record by ID
    Optional<PortCongestionData> findById(Integer id);

    // Fetch all records
    List<PortCongestionData> findAll();

    // Filter by situation type
    List<PortCongestionData> findBySituationType(SituationType type);

    // Delete record by ID
    void deleteById(Integer id);

    // Check existence by ID
    boolean existsById(Integer id);

    // Count by situation type
    int countBySituationType(SituationType type);
}
