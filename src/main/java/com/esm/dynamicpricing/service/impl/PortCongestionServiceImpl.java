package com.esm.dynamicpricing.service.impl;

import com.esm.dynamicpricing.exception.ValidationException;
import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;
import com.esm.dynamicpricing.model.PortEventFieldConfig;
import com.esm.dynamicpricing.repository.PortCongestionRepository;
import com.esm.dynamicpricing.repository.PortEventFieldConfigRepository;
import com.esm.dynamicpricing.service.EventDateTimeService;
import com.esm.dynamicpricing.service.PortCongestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PortCongestionServiceImpl implements PortCongestionService {

    private final PortCongestionRepository repository;
    private final PortEventFieldConfigRepository configRepository;
    private final EventDateTimeService dateTimeService;

    @Override
    public PortCongestionData save(PortCongestionData data, String userTimezone) {
        log.info("Saving PortCongestionData: {}", data);

        // Validate situationType
        SituationType type = data.getSituationType();
        if (type == null) {
            throw new ValidationException("SituationType is required");
        }

        // Fetch field configs for this situationType
        List<PortEventFieldConfig> configs = configRepository
                .findByEventTypeOrderByDisplayOrder(type.name());

        if (configs.isEmpty()) {
            throw new ValidationException("No field configuration found for situationType: " + type);
        }

        // Process eventDetails with validation
        Map<String, Object> processedEventDetails = processEventDetails(
                data.getDetails(), configs, userTimezone);
        data.setDetails(processedEventDetails);

        return repository.save(data);
    }

    private Map<String, Object> processEventDetails(
            Map<String, Object> inputDetails,
            List<PortEventFieldConfig> configs,
            String userTimezone) {

        Map<String, Object> processed = new HashMap<>();

        for (PortEventFieldConfig config : configs) {
            String fieldName = config.getFieldName();
            Object value = inputDetails.get(fieldName);

            // Validate required fields
            if (Boolean.TRUE.equals(config.getIsRequired()) &&
                    (value == null || value.toString().trim().isEmpty())) {
                throw new ValidationException("Required field missing: " + fieldName);
            }

            if (value != null) {
                switch (config.getFieldType()) {
                    case DATETIME:
                        dateTimeService.storeDateTimeField(processed, fieldName,
                                value.toString(), userTimezone);
                        break;
                    case NUMBER:
                        processed.put(fieldName, parseNumber(value));
                        break;
                    case BOOLEAN:
                        processed.put(fieldName, Boolean.valueOf(value.toString()));
                        break;
                    default:
                        processed.put(fieldName, value.toString());
                }
            }
        }

        return processed;
    }

    private Number parseNumber(Object value) {
        try {
            if (value instanceof Number) {
                return (Number) value;
            } else {
                return Double.parseDouble(value.toString());
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid number format: " + value);
        }
    }

    @Override
    public Optional<PortCongestionData> findById(Integer id) {
        log.debug("Fetching PortCongestionData by id: {}", id);
        return repository.findById(id);
    }

    @Override
    public List<PortCongestionData> findAll() {
        log.debug("Fetching all PortCongestionData records");
        return repository.findAll();
    }

    @Override
    public List<PortCongestionData> findBySituationType(SituationType type) {
        log.debug("Fetching PortCongestionData by situationType: {}", type);
        return repository.findBySituationType(type);
    }

    @Override
    public void deleteById(Integer id) {
        log.warn("Deleting PortCongestionData with id: {}", id);
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        log.debug("Checking existence of PortCongestionData with id: {}", id);
        return repository.existsById(id);
    }

    @Override
    public int countBySituationType(SituationType type) {
        log.debug("Counting PortCongestionData records for situationType: {}", type);
        return repository.countBySituationType(type);
    }
}
