package com.esm.dynamicpricing.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortCongestionData {

    private Integer id;
    private String port;
    private String terminal;
    private SituationType situationType;

    private OffsetDateTime startEvent;
    private OffsetDateTime endEvent;
    private OffsetDateTime startEffected;
    private OffsetDateTime endEffected;

    private Integer maxCrane;
    private Integer operatingCrane;
    private Integer maxTruckVessel;
    private Integer operatingTruckVessel;
    private Integer maxBerth;           
    private Integer availableBerth;     
    private BigDecimal stevedoreShortagePercent;
    private BigDecimal normalFormalityHrs;
    private BigDecimal abnormalFormalityHrs;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
