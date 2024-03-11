package com.mavi.memorize.api

import com.mavi.memorize.data.entity.MemorizeRecord

interface MemorizeRecordsApi {
    fun findByMonth(year: Int, month: Int): List<MemorizeRecord>
    fun recordMemorizeWords(words: List<String>): List<MemorizeRecord>
}
