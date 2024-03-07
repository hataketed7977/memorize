package com.mavi.memorize.data

import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.data.entity.FamiliarWord
import com.mavi.memorize.data.repository.FamiliarWordRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Component
@Transactional
class FamiliarWordsApiImpl(
    private val familiarWordRepository: FamiliarWordRepository
) : FamiliarWordsApi {
    override fun count(): Long = familiarWordRepository.count()

    override fun addFamiliarWord(vocabularyId: String): FamiliarWord {
        val entity = FamiliarWord()
        entity.id = UUID.randomUUID().toString()
        entity.vocabularyId = vocabularyId
        entity.createdAt = Instant.now()
        return familiarWordRepository.saveAndFlush(entity)
    }

    override fun deleteByVocabularyId(id: String) {
        familiarWordRepository.deleteByVocabularyId(id)
    }
}
