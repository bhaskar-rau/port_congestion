package com.esm.dynamicpricing.service;

import com.esm.dynamicpricing.model.PortCongestionData;

import java.time.LocalDate;
import java.util.List;

public interface SummaryService {
    List<PortCongestionData> getSummary(String port, String terminal,
                                        LocalDate minDate, LocalDate maxDate,
                                        int page, int size);

    int countSummary(String port, String terminal, LocalDate minDate, LocalDate maxDate);

    PortCongestionData updateRecord(Integer id, PortCongestionData updated);

    void deleteRecord(Integer id);
}
