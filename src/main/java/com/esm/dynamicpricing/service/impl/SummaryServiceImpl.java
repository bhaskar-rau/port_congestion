package com.esm.dynamicpricing.service.impl;

import com.esm.dynamicpricing.exception.ResourceNotFoundException;
import com.esm.dynamicpricing.exception.ValidationException;
import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.repository.SummaryRepository;
import com.esm.dynamicpricing.service.SummaryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final SummaryRepository repository;

    public SummaryServiceImpl(SummaryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PortCongestionData> getSummary(String port, String terminal,
                                               LocalDate minDate, LocalDate maxDate,
                                               int page, int size) {
        if (minDate == null && maxDate == null) {
            maxDate = LocalDate.now();
            minDate = maxDate.minusDays(7);
        }
        int offset = (page - 1) * size;
        return repository.findAll(port, terminal, minDate, maxDate, offset, size);
    }

    @Override
    public int countSummary(String port, String terminal, LocalDate minDate, LocalDate maxDate) {
        return repository.count(port, terminal, minDate, maxDate);
    }

    @Override
    public PortCongestionData updateRecord(Integer id, PortCongestionData updated) {
        PortCongestionData existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id " + id));

        // --- Validation Rules ---

        // 1. Dates must make sense
        if (updated.getStartEvent() != null && updated.getEndEvent() != null
                && updated.getStartEvent().isAfter(updated.getEndEvent())) {
            throw new ValidationException("Start Event cannot be after End Event");
        }

        if (updated.getStartEffected() != null && updated.getEndEffected() != null
                && updated.getStartEffected().isAfter(updated.getEndEffected())) {
            throw new ValidationException("Start Effected cannot be after End Effected");
        }

        // 2. Mandatory fields by situation type
        if (updated.getSituationType() != null) {
            switch (updated.getSituationType()) {
                case CRANE_DOWN -> {
                    if (updated.getMaxCrane() == null || updated.getOperatingCrane() == null) {
                        throw new ValidationException("Crane Down requires Max Crane and Operating Crane values");
                    }
                }
                case TRUCK_MOVEMENT, CY_UTLZ_OVER_100 -> {
                    if (updated.getMaxTruckVessel() == null || updated.getOperatingTruckVessel() == null) {
                        throw new ValidationException("Truck Movement/CY Utilization requires Max and Operating Truck/Vessel");
                    }
                }
                case TERMINAL_MAINTENANCE -> {
                    if (updated.getMaxBerth() == null || updated.getAvailableBerth() == null) {
                        throw new ValidationException("Terminal Maintenance Plan requires Max and Available Berths");
                    }
                }
                case STEVEDORE_SHORTAGE -> {
                    if (updated.getStevedoreShortagePercent() == null) {
                        throw new ValidationException("Stevedore Shortage requires shortage percentage");
                    }
                }
                case PORT_FORMALITY_INCREASING -> {
                    if (updated.getNormalFormalityHrs() == null || updated.getAbnormalFormalityHrs() == null) {
                        throw new ValidationException("Port Formality Increasing requires Normal and Abnormal Formality hours");
                    }
                }
            }
        }

        repository.updateRecord(id, updated);
        return repository.findById(id).get();
    }

    @Override
    public void deleteRecord(Integer id) {
        int rows = repository.deleteById(id);
        if (rows == 0) {
            throw new ResourceNotFoundException("Record not found with id " + id);
        }
    }
}
