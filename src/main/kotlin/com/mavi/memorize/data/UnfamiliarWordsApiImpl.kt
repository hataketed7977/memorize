package com.mavi.memorize.data

import com.mavi.memorize.api.UnfamiliarWordsApi
import com.mavi.memorize.data.entity.UnfamiliarWord
import com.mavi.memorize.data.repository.UnfamiliarWordRepository
import com.mavi.memorize.data.repository.VocabularyRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Component
@Transactional
class UnfamiliarWordsApiImpl(
    private val vocabularyRepository: VocabularyRepository,
    private val unfamiliarWordRepository: UnfamiliarWordRepository
) : UnfamiliarWordsApi {
    override fun count(): Long = unfamiliarWordRepository.count()
    override fun batchCreateUnfamiliarWords(number: Int): List<UnfamiliarWord> {
        val ids = findAll().map { it.vocabularyId }
        val pageable = Pageable.ofSize(number)
        val words = if (ids.isEmpty()) {
            vocabularyRepository.findAllByStudyIsFalse(pageable)
        } else {
            vocabularyRepository.findAllByStudyIsFalseAndIdNotIn(ids, pageable)
        }.map {
            val entity = UnfamiliarWord()
            entity.id = UUID.randomUUID().toString()
            entity.vocabularyId = it.id
            entity.createdAt = Instant.now()
            entity
        }.content
        if (words.isEmpty()) return emptyList()
        return unfamiliarWordRepository.saveAllAndFlush(words)
    }

    override fun findAll(): List<UnfamiliarWord> {
        return unfamiliarWordRepository.findAll()
    }

    override fun findByVocabularyId(vocabularyId: String): Optional<UnfamiliarWord> {
        return unfamiliarWordRepository.findByVocabularyId(vocabularyId)
    }

    override fun deleteByVocabularyId(id: String) {
        unfamiliarWordRepository.deleteByVocabularyId(id)
    }
}
