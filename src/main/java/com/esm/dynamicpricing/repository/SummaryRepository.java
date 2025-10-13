package com.esm.dynamicpricing.repository;

import com.esm.dynamicpricing.model.PortCongestionData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SummaryRepository {
    List<PortCongestionData> findAll(String port, String terminal,
                                     LocalDate minDate, LocalDate maxDate,
                                     int offset, int limit);

    int count(String port, String terminal, LocalDate minDate, LocalDate maxDate);

    Optional<PortCongestionData> findById(Integer id);

    int updateRecord(Integer id, PortCongestionData record);

    int deleteById(Integer id);
}
