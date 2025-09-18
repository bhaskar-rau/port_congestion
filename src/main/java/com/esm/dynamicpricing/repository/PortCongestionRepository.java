package com.esm.dynamicpricing.repository;

import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PortCongestionRepository {
    PortCongestionData save(PortCongestionData data);
    Optional<PortCongestionData> findById(Integer id);
    List<PortCongestionData> findAll();
    List<PortCongestionData> findBySituationType(SituationType type);
    void deleteById(Integer id);

        // Check if record exists by ID
    boolean existsById(Integer id);

    // Count records for a given situation type
    int countBySituationType(SituationType situationType);


        // Find by situationType and creation date range
    List<PortCongestionData> findBySituationTypeAndCreatedAtBetween(
            SituationType type, OffsetDateTime start, OffsetDateTime end);

    // Find by single event detail key-value
    List<PortCongestionData> findBySituationTypeAndEventDetail(
            SituationType type, String fieldName, String fieldValue);

    // Find by multiple event details (JSONB containment)
    List<PortCongestionData> findBySituationTypeAndEventDetailsContaining(
            SituationType type, Map<String, Object> eventDetailFilters);
}
