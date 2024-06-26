package com.mavi.memorize.data

import com.mavi.memorize.api.IncorrectWordsApi
import com.mavi.memorize.data.entity.IncorrectWord
import com.mavi.memorize.data.entity.view.IncorrectVocabulary
import com.mavi.memorize.data.repository.IncorrectWordRepository
import com.mavi.memorize.data.repository.VocabularyRepository
import com.mavi.memorize.data.repository.view.IncorrectVocabularyRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Component
@Transactional
class IncorrectWordsApiImpl(
    private val incorrectWordRepository: IncorrectWordRepository,
    private val incorrectVocabularyRepository: IncorrectVocabularyRepository,
    private val vocabularyRepository: VocabularyRepository
) : IncorrectWordsApi {
    override fun count(): Long = incorrectWordRepository.countByCountGreaterThan(0)
    override fun addIncorrectWord(vocabularyId: String): IncorrectWord? {
        if (vocabularyRepository.findById(vocabularyId).isEmpty) return null
        val word = incorrectWordRepository
            .findByVocabularyId(vocabularyId)
            .getOrElse {
                val entity = IncorrectWord()
                entity.id = UUID.randomUUID().toString()
                entity.vocabularyId = vocabularyId
                entity.createdAt = Instant.now()
                entity.updatedAt = Instant.now()
                entity.count = 0
                incorrectWordRepository.saveAndFlush(entity)
            }
        word.count += 1
        word.updatedAt = Instant.now()
        return incorrectWordRepository.saveAndFlush(word)
    }

    override fun deleteByVocabularyId(id: String) {
        incorrectWordRepository.deleteByVocabularyId(id)
    }

    override fun findAllByCountGreaterThanZero(): List<IncorrectWord> {
        return incorrectWordRepository.findAllByCountGreaterThan(0)
    }

    override fun reduceCountByVocabularyId(vocabularyId: String): Boolean {
        var hasReduce = false
        incorrectWordRepository.findByVocabularyId(vocabularyId).ifPresent {
            it.count -= 1
            incorrectWordRepository.saveAndFlush(it)
            hasReduce = true
        }
        return hasReduce
    }

    override fun findByVocabularyId(vocabularyId: String): Optional<IncorrectWord> {
        return incorrectWordRepository.findByVocabularyId(vocabularyId)
    }

    override fun findIncorrectVocabularies(pageable: Pageable): Page<IncorrectVocabulary> {
        return incorrectVocabularyRepository.findAllByCountGreaterThan(0, pageable)
    }
}
