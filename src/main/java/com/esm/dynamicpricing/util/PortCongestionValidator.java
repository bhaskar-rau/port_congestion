package com.esm.dynamicpricing.util;

import org.springframework.stereotype.Component;
import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;

@Component
public class PortCongestionValidator {

    public void validate(PortCongestionData data) {
        SituationType type = data.getSituationType();

        if (type == null) {
            throw new IllegalArgumentException("situationType is required");
        }

        switch (type) {
            case PORT_CLOSE:
                require(data.getStartEvent(), "startEvent");
                require(data.getEndEvent(), "endEvent");
                require(data.getStartEffected(), "startEffected");
                require(data.getEndEffected(), "endEffected");
                break;

            case CRANE_DOWN:
                require(data.getStartEvent(), "startEvent");
                require(data.getEndEvent(), "endEvent");
                require(data.getMaxCrane(), "maxCrane");
                require(data.getOperatingCrane(), "operatingCrane");
                break;

            case TRUCK_MOVEMENT:
                require(data.getStartEvent(), "startEvent");
                require(data.getEndEvent(), "endEvent");
                require(data.getMaxTruckVessel(), "maxTruckVessel");
                require(data.getOperatingTruckVessel(), "operatingTruckVessel");
                break;

            case CY_UTLZ_OVER_100:
                require(data.getStartEvent(), "startEvent");
                require(data.getEndEvent(), "endEvent");
                require(data.getMaxTruckVessel(), "maxTruckVessel");
                require(data.getOperatingTruckVessel(), "operatingTruckVessel");
                break;

            case TERMINAL_MAINTENANCE:
                require(data.getStartEvent(), "startEvent");
                require(data.getEndEvent(), "endEvent");
                require(data.getMaxBerth(), "maxBerth");          // updated field name
                require(data.getAvailableBerth(), "availableBerth"); // updated field name
                break;

            case STEVEDORE_SHORTAGE:
                require(data.getStartEvent(), "startEvent");
                require(data.getEndEvent(), "endEvent");
                require(data.getStevedoreShortagePercent(), "stevedoreShortagePercent");
                break;

            case PORT_FORMALITY_INCREASING:
                require(data.getStartEvent(), "startEvent");
                require(data.getEndEvent(), "endEvent");
                require(data.getNormalFormalityHrs(), "normalFormalityHrs");
                require(data.getAbnormalFormalityHrs(), "abnormalFormalityHrs");
                break;

            default:
                throw new IllegalArgumentException("Unknown situationType: " + type);
        }
    }

    private void require(Object value, String field) {
        if (value == null) {
            throw new IllegalArgumentException("Field " + field + " is required for this situation type");
        }
    }
}
