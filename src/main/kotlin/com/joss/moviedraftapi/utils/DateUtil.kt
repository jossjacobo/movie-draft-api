package com.joss.moviedraftapi.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {

    /**
     * The ISO-like date-time formatter that formats or parses a date-time with
     * the offset and zone if available, such as '2011-12-03T10:15:30',
     * '2011-12-03T10:15:30+01:00' or '2011-12-03T10:15:30+01:00[Europe/Paris]'.
     */
    fun now(): String = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())

}