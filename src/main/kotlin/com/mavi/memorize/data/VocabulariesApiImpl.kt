package com.mavi.memorize.data

import com.mavi.memorize.api.*
import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.data.repository.VocabularyRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Component
@Transactional
class VocabulariesApiImpl(
    private val vocabularyRepository: VocabularyRepository,
    private val familiarWordsApi: FamiliarWordsApi,
    private val unfamiliarWordsApi: UnfamiliarWordsApi,
    private val incorrectWordsApi: IncorrectWordsApi
) : VocabulariesApi {
    companion object {
        private val familiarWordsReviewCycleDays = listOf<Long>(2, 4, 7, 20)
    }

    override fun findById(id: String): Optional<Vocabulary> {
        return vocabularyRepository.findByIdAndDelIsFalse(id)
    }

    override fun addVocabulary(request: AddVocabularyRequest): Vocabulary {
        val entity = Vocabulary()
        entity.id = UUID.randomUUID().toString()
        entity.word = request.word
        entity.meaning = request.meaning
        entity.pron = request.pron
        entity.partOfSpeech = request.partOfSpeech
        entity.sentence = request.sentence
        entity.createdAt = Instant.now()
        entity.study = false
        entity.del = false
        return vocabularyRepository.saveAndFlush(entity)
    }

    override fun findByPage(word: String?, study: Boolean?, pageRequest: PageRequest): Page<Vocabulary> {
        return vocabularyRepository.findByPage(word, study, pageRequest)
    }

    override fun removeVocabularyById(id: String) {
        findById(id).ifPresent {
            it.del = true
            updateVocabulary(it)
            familiarWordsApi.deleteByVocabularyId(id)
            unfamiliarWordsApi.deleteByVocabularyId(id)
            incorrectWordsApi.deleteByVocabularyId(id)
        }
    }

    override fun updateVocabulary(item: Vocabulary): Vocabulary {
        return vocabularyRepository.saveAndFlush(item)
    }

    override fun count(): Triple<All, Study, NotStudy> {
        return Triple(
            first = vocabularyRepository.countByDelIsFalse(),
            second = vocabularyRepository.countByDelIsFalseAndStudyIsTrue(),
            third = vocabularyRepository.countByDelIsFalseAndStudyIsFalse(),
        )
    }

    override fun findAllByIds(ids: List<String>, pageable: Pageable): Page<Vocabulary> {
        return vocabularyRepository.findAllByIdInAndDelIsFalse(ids, pageable)
    }

    override fun findExamVocabularies(): List<Vocabulary> {
        val unfamiliarIds = unfamiliarWordsApi.findAll().map { it.vocabularyId }
        val incorrectIds = incorrectWordsApi.findAllByCountGreaterThanZero().map { it.vocabularyId }
        val familiarIds = familiarWordsReviewCycleDays.mapIndexed { index, day ->
            familiarWordsApi.findByRound(index + 1).filter { hasPassedDay(it.createdAt, day) }
        }.flatten().map { it.vocabularyId }

        return findAllByIds(
            (unfamiliarIds + incorrectIds + familiarIds).distinct(),
            Pageable.ofSize(Int.MAX_VALUE)
        ).content
    }

    private fun hasPassedDay(instant: Instant, day: Long) =
        instant.plus(day, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)
            .isBefore(Instant.now().truncatedTo(ChronoUnit.DAYS))

    override fun checkExamVocabularies(filled: Map<String, String>) {
        val ids = filled.map { it.key }.toList()
        vocabularyRepository.findAllByIdIn(ids)
            .filter { filled[it.id] != null }
            .forEach {
                val word = filled[it.id]
                if (it.word.lowercase() == word?.lowercase()) {
                    familiarWordsApi.addFamiliarWord(it.id)
                    incorrectWordsApi.reduceCountByVocabularyId(it.id)
                } else {
                    incorrectWordsApi.addIncorrectWord(it.id)
                }
                unfamiliarWordsApi.deleteByVocabularyId(it.id)
                it.study = true
                updateVocabulary(it)
            }
    }
}
