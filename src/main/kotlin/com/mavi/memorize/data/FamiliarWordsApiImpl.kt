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
        val familiarWord = familiarWordRepository.findByVocabularyId(vocabularyId)
        return if (familiarWord.isPresent) {
            updateRound(familiarWord.get())
        } else {
            val entity = FamiliarWord()
            entity.id = UUID.randomUUID().toString()
            entity.vocabularyId = vocabularyId
            entity.createdAt = Instant.now()
            familiarWordRepository.saveAndFlush(entity)
        }
    }

    override fun deleteByVocabularyId(id: String) {
        familiarWordRepository.deleteByVocabularyId(id)
    }

    override fun findAllNotStartRounds(): List<FamiliarWord> {
        return familiarWordRepository.findAllByRound4IsNull()
    }

    override fun findByVocabularyId(vocabularyId: String): Optional<FamiliarWord> {
        return familiarWordRepository.findByVocabularyId(vocabularyId)
    }

    override fun update(familiarWord: FamiliarWord): FamiliarWord {
        return familiarWordRepository.saveAndFlush(familiarWord)
    }

    override fun updateRoundByVocabularyId(vocabularyId: String) {
        return familiarWordRepository.findByVocabularyId(vocabularyId).ifPresent { updateRound(it) }
    }

    private fun updateRound(entity: FamiliarWord): FamiliarWord {
        when {
            entity.round1 == null -> entity.round1 = Instant.now()
            entity.round2 == null -> entity.round2 = Instant.now()
            entity.round3 == null -> entity.round3 = Instant.now()
            entity.round4 == null -> entity.round4 = Instant.now()
        }
        return familiarWordRepository.saveAndFlush(entity)
    }
}
