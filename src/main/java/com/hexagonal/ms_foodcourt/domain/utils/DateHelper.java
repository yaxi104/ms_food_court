package com.hexagonal.ms_foodcourt.domain.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ZONA_BOGOTA;

public class DateHelper {

    private DateHelper() {
    }

    public static LocalDateTime dateBogota() {
        return ZonedDateTime.now(ZoneId.of(ZONA_BOGOTA)).toLocalDateTime();
    }
}
