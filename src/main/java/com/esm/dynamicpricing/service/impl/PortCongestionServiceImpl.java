package com.esm.dynamicpricing.service.impl;

import com.esm.dynamicpricing.exception.ValidationException;
import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;
import com.esm.dynamicpricing.repository.PortCongestionRepository;
import com.esm.dynamicpricing.service.EventDateTimeService;
import com.esm.dynamicpricing.service.PortCongestionService;
import com.esm.dynamicpricing.util.PortCongestionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PortCongestionServiceImpl implements PortCongestionService {

    private final PortCongestionRepository repository;
    private final EventDateTimeService dateTimeService;
    private final PortCongestionValidator validator;

   @Override
public PortCongestionData save(PortCongestionData data, String userTimezone) {
    log.info("Saving PortCongestionData: {}", data);

    try {
        try {
            validator.validate(data);
        } catch (IllegalArgumentException iae) {
            throw new ValidationException(iae.getMessage(), iae);
        }

        if (data.getStartEvent() != null) {
            data.setStartEvent(dateTimeService.toUtc(data.getStartEvent(), userTimezone));
        }
        if (data.getEndEvent() != null) {
            data.setEndEvent(dateTimeService.toUtc(data.getEndEvent(), userTimezone));
        }
        if (data.getStartEffected() != null) {
            data.setStartEffected(dateTimeService.toUtc(data.getStartEffected(), userTimezone));
        }
        if (data.getEndEffected() != null) {
            data.setEndEffected(dateTimeService.toUtc(data.getEndEffected(), userTimezone));
        }

        return repository.save(data);

    } catch (ValidationException ve) {
        log.warn("Validation failed for PortCongestionData: {}", data, ve);
        throw ve; 

    } catch (DataIntegrityViolationException dive) {
        log.error("Database constraint violation while saving: {}", data, dive);
        throw new RuntimeException("Database constraint violation", dive);

    } catch (Exception e) {
        log.error("Unexpected error saving PortCongestionData: {}", data, e);
        throw new RuntimeException("Error saving port congestion data", e);
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
