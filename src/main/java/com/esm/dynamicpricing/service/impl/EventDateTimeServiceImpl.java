package com.esm.dynamicpricing.service.impl;

import com.esm.dynamicpricing.service.EventDateTimeService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class EventDateTimeServiceImpl implements EventDateTimeService {

    @Override
    public OffsetDateTime toUtc(OffsetDateTime input, String userTimezone) {
        if (input == null) return null;

        ZoneId userZone = ZoneId.of(userTimezone);  
        ZonedDateTime zoned = input.atZoneSameInstant(userZone);
        return zoned.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime();
    }

    @Override
    public OffsetDateTime toUserTime(OffsetDateTime input, String userTimezone) {
        if (input == null) return null;

        ZoneId userZone = ZoneId.of(userTimezone);
        return input.atZoneSameInstant(ZoneId.of("UTC"))
                    .withZoneSameInstant(userZone)
                    .toOffsetDateTime();
    }
}
