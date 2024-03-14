package com.mavi.memorize.data

import com.mavi.memorize.api.MemorizeRecordsApi
import com.mavi.memorize.data.entity.view.FamiliarVocabulary
import com.mavi.memorize.data.repository.view.FamiliarVocabularyRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.ZoneId

@Component
@Transactional
class MemorizeRecordsApiImpl(
    private val familiarVocabularyRepository: FamiliarVocabularyRepository
) : MemorizeRecordsApi {
    override fun findByMonth(year: Int, month: Int): List<MemorizeRecord> {
        val startOfMonth = LocalDate.of(year, month, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endOfMonth = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1)
            .atTime(23, 59, 59, 999999999).atZone(ZoneId.systemDefault()).toInstant()

        return familiarVocabularyRepository.findAllByFamiliarCreatedAtBetween(startOfMonth, endOfMonth)
            .groupBy { it.familiarCreatedAt.atZone(ZoneId.systemDefault()).toLocalDate().dayOfMonth }
            .map {
                MemorizeRecord(
                    year = year,
                    month = month,
                    day = it.key,
                    words = it.value
                )
            }
    }
}

data class MemorizeRecord(
    val year: Int,
    val month: Int,
    val day: Int,
    val words: List<FamiliarVocabulary>
)
