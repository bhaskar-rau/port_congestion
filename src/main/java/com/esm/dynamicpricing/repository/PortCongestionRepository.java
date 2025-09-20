package com.esm.dynamicpricing.repository;

import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface PortCongestionRepository {
    PortCongestionData save(PortCongestionData data);
    Optional<PortCongestionData> findById(Integer id);
    List<PortCongestionData> findAll();
    List<PortCongestionData> findBySituationType(SituationType type);
    void deleteById(Integer id);

    boolean existsById(Integer id);
    int countBySituationType(SituationType situationType);

    List<PortCongestionData> findBySituationTypeAndCreatedAtBetween(
            SituationType type, OffsetDateTime start, OffsetDateTime end);

    // Search by any one column dynamically
    List<PortCongestionData> findBySituationTypeAndField(
            SituationType type, String fieldName, Object fieldValue);

    // Flexible filter across multiple fields
    List<PortCongestionData> findBySituationTypeAndFields(
            SituationType type, java.util.Map<String, Object> filters);
}
