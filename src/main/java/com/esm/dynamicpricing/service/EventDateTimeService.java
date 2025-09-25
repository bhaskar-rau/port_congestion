package com.esm.dynamicpricing.service;

import java.time.OffsetDateTime;

public interface EventDateTimeService {

    OffsetDateTime toUtc(OffsetDateTime input, String userTimezone);


    OffsetDateTime toUserTime(OffsetDateTime input, String userTimezone);
}
