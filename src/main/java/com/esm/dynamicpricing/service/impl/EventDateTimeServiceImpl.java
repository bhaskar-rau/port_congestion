package com.esm.dynamicpricing.service.impl;

import com.esm.dynamicpricing.service.EventDateTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.esm.dynamicpricing.exception.ValidationException;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Service
@Slf4j
public class EventDateTimeServiceImpl implements EventDateTimeService {

    @Override
    public void storeDateTimeField(Map<String, Object> eventDetails, String fieldName, String dateTimeValue, String userTimezone) {
        try {
            // Parse input datetime with user timezone
            ZonedDateTime userZdt = ZonedDateTime.parse(dateTimeValue).withZoneSameInstant(ZoneId.of(userTimezone));

            // Convert to UTC and store as ISO string
            String utcDateTime = userZdt.withZoneSameInstant(ZoneOffset.UTC).toString();
            eventDetails.put(fieldName, utcDateTime);

            // Optionally store original timezone
            eventDetails.put(fieldName + "_original_timezone", userTimezone);

        } catch (DateTimeParseException e) {
            log.error("Failed to parse datetime field {}: {}", fieldName, e.getMessage());
            throw new ValidationException("Invalid datetime format for field: " + fieldName);
        }
    }

    @Override
    public ZonedDateTime retrieveDateTimeField(Map<String, Object> eventDetails, String fieldName, String targetTimezone) {
        String utcDateTime = (String) eventDetails.get(fieldName);
        if (utcDateTime == null) return null;

        try {
            return ZonedDateTime.parse(utcDateTime)
                    .withZoneSameInstant(ZoneId.of(targetTimezone));
        } catch (DateTimeParseException e) {
            log.error("Failed to parse stored UTC datetime for field {}: {}", fieldName, e.getMessage());
            return null;
        }
    }
}
