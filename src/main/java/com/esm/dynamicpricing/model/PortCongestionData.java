package com.esm.dynamicpricing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortCongestionData {
    private Integer id;                 
    private String port;                
    private String terminal;            
    private SituationType situationType; 
    private Map<String, Object> details; 
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
