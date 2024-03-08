package com.mavi.memorize.data.helper

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateHelper {
    companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.of("Asia/Shanghai"))

        fun format(instant: Instant?, formatter: DateTimeFormatter = DateHelper.formatter): String {
            return if (instant == null) ""
            else formatter.format(instant)
        }
    }
}
