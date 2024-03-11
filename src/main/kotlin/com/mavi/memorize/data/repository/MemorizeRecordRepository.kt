package com.mavi.memorize.data.repository

import com.mavi.memorize.data.entity.MemorizeRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemorizeRecordRepository : JpaRepository<MemorizeRecord, String> {
    fun findByYearAndMonth(year: Int, month: Int): List<MemorizeRecord>
}
