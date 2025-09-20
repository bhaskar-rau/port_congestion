package com.esm.dynamicpricing.service;

import java.time.OffsetDateTime;

public interface EventDateTimeService {

    /**
     * Convert a datetime from user timezone to UTC for DB persistence.
     */
    OffsetDateTime toUtc(OffsetDateTime input, String userTimezone);

    /**
     * Convert a datetime from UTC (DB) to user timezone for display.
     */
    OffsetDateTime toUserTime(OffsetDateTime input, String userTimezone);
}
