package com.mavi.memorize.data

import com.mavi.memorize.api.IncorrectWordsApi
import com.mavi.memorize.data.entity.IncorrectWord
import com.mavi.memorize.data.repository.IncorrectWordRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Component
@Transactional
class IncorrectWordsApiImpl(
    private val incorrectWordRepository: IncorrectWordRepository
) : IncorrectWordsApi {
    override fun count(): Long = incorrectWordRepository.count()
    override fun addIncorrectWord(vocabularyId: String): IncorrectWord {
        val word = incorrectWordRepository
            .findByVocabularyId(vocabularyId)
            .getOrElse {
                val entity = IncorrectWord()
                entity.id = UUID.randomUUID().toString()
                entity.vocabularyId = vocabularyId
                entity.createdAt = Instant.now()
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
}
