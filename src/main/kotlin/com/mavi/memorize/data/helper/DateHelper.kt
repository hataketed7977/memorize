package com.mavi.memorize.data.helper

import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateHelper {
    companion object {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.of("Asia/Shanghai"))
    }
}
