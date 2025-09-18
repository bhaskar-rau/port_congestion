package com.esm.dynamicpricing.service;

import java.time.ZonedDateTime;
import java.util.Map;

public interface EventDateTimeService {

    /**
     * Store a datetime value in the eventDetails map with proper UTC conversion.
     *
     * @param eventDetails the map storing event fields
     * @param fieldName the name of the datetime field
     * @param dateTimeValue the datetime value as string
     * @param userTimezone the user's timezone
     */
    void storeDateTimeField(Map<String, Object> eventDetails, String fieldName, String dateTimeValue, String userTimezone);

    /**
     * Retrieve a datetime value from the eventDetails map in a target timezone.
     *
     * @param eventDetails the map storing event fields
     * @param fieldName the name of the datetime field
     * @param targetTimezone the desired timezone
     * @return the datetime value as ZonedDateTime or null if not present
     */
    ZonedDateTime retrieveDateTimeField(Map<String, Object> eventDetails, String fieldName, String targetTimezone);
}

