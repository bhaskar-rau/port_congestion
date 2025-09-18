package com.esm.dynamicpricing.repository;

import com.esm.dynamicpricing.model.PortEventFieldConfig;

import java.util.List;

public interface PortEventFieldConfigRepository {
    
    PortEventFieldConfig save(PortEventFieldConfig config);
    
    List<PortEventFieldConfig> findByEventTypeOrderByDisplayOrder(String eventType);
    
    void deleteById(Integer id);
    
    boolean existsById(Integer id);
}
