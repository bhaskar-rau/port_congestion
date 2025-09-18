package com.esm.dynamicpricing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortEventFieldConfig {

    private Integer id;                       // Primary Key (auto-generated)
    private String eventType;                 // e.g. "Port Close"
    private String fieldName;                 // e.g. "Start Event DateTime"
    private FieldType fieldType;              // Enum: TEXT, DATETIME, NUMBER, BOOLEAN, SELECT
    private Boolean isRequired;               // Default: false
    private Map<String, Object> validationRules; // JSONB → mapped as Map<String,Object>
    private Integer displayOrder;             // Order for UI display
    private OffsetDateTime createdAt;         // Auto-set by DB
}
