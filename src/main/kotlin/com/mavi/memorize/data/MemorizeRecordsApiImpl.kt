package com.mavi.memorize.data

import com.mavi.memorize.api.MemorizeRecordsApi
import com.mavi.memorize.data.entity.MemorizeRecord
import com.mavi.memorize.data.repository.MemorizeRecordRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Component
@Transactional
class MemorizeRecordsApiImpl(private val repository: MemorizeRecordRepository) : MemorizeRecordsApi {
    override fun findByMonth(year: Int, month: Int): List<MemorizeRecord> {
        return repository.findByYearAndMonth(year, month)
    }

    override fun recordMemorizeWords(words: List<String>): List<MemorizeRecord> {
        val date = LocalDate.now()
        return repository.saveAll(words.map {
            val entity = MemorizeRecord()
            entity.id = UUID.randomUUID().toString()
            entity.words = words
            entity.createdAt = Instant.now()
            entity.year = date.year
            entity.month = date.monthValue
            entity.day = date.dayOfMonth
            entity
        })
    }
}
