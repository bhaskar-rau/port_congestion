package com.esm.dynamicpricing.service;

import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;

import java.util.List;
import java.util.Optional;

public interface PortCongestionService {

    PortCongestionData save(PortCongestionData data, String userTimezone);

    Optional<PortCongestionData> findById(Integer id);

    List<PortCongestionData> findAll();

    List<PortCongestionData> findBySituationType(SituationType type);

    void deleteById(Integer id);

    boolean existsById(Integer id);

    int countBySituationType(SituationType type);



}
