package com.mavi.memorize.api

import com.mavi.memorize.data.MemorizeRecord

fun interface MemorizeRecordsApi {
    fun findByMonth(year: Int, month: Int): List<MemorizeRecord>
}
