package com.mavi.memorize.data

import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.data.entity.FamiliarWord
import com.mavi.memorize.data.entity.view.FamiliarVocabulary
import com.mavi.memorize.data.repository.FamiliarWordRepository
import com.mavi.memorize.data.repository.VocabularyRepository
import com.mavi.memorize.data.repository.view.FamiliarVocabularyRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Component
@Transactional
class FamiliarWordsApiImpl(
    private val familiarWordRepository: FamiliarWordRepository,
    private val familiarVocabularyRepository: FamiliarVocabularyRepository,
    private val vocabularyRepository: VocabularyRepository
) : FamiliarWordsApi {
    override fun count(): Long = familiarWordRepository.count()

    override fun addFamiliarWord(vocabularyId: String, fromIncorrectWords: Boolean): FamiliarWord? {
        if (vocabularyRepository.findById(vocabularyId).isEmpty) return null
        val familiarWord = familiarWordRepository.findByVocabularyId(vocabularyId)
        return if (familiarWord.isPresent) {
            if (fromIncorrectWords) {
                clearRound(familiarWord.get())
            } else {
                updateRound(familiarWord.get())
            }
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

    override fun findFamiliarVocabularies(pageable: Pageable): Page<FamiliarVocabulary> {
        return familiarVocabularyRepository.findAll(pageable)
    }

    override fun removeByVocabularyId(vocabularyId: String) {
        deleteByVocabularyId(vocabularyId)
        vocabularyRepository.findById(vocabularyId).ifPresent {
            it.study = false
            vocabularyRepository.saveAndFlush(it)
        }
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

    private fun clearRound(entity: FamiliarWord): FamiliarWord {
        entity.round1 = null
        entity.round2 = null
        entity.round3 = null
        entity.round4 = null
        entity.createdAt = Instant.now()
        return familiarWordRepository.saveAndFlush(entity)
    }
}
