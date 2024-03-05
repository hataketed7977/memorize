package com.mavi.memorize.domain.association.db

import com.mavi.memorize.domain.association.db.po.VocabularyPO
import com.mavi.memorize.domain.association.db.po.toModel
import com.mavi.memorize.domain.association.db.repository.VocabularyRepository
import com.mavi.memorize.domain.description.VocabularyDescription
import com.mavi.memorize.domain.model.Vocabularies
import com.mavi.memorize.domain.model.Vocabulary
import com.mavi.memorize.domain.model.VocabularyId
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.util.*

@Component
class VocabulariesImpl(
    private val vocabularyRepository: VocabularyRepository
) : Vocabularies {
    companion object {
        private const val MAX_PAGE_SIZE = 1000
        private val DEFAULT_SORT = Sort.by(Sort.Order.desc("createdAt"))
    }

    override fun addVocabulary(description: VocabularyDescription): Vocabulary {
        val po = VocabularyPO()
        po.id = UUID.randomUUID().toString()
        po.word = description.word
        po.meaning = description.meaning
        po.pron = description.pron
        po.partOfSpeech = description.partOfSpeech
        po.study = description.study
        po.createdAt = description.createdAt
        po.del = false
        return vocabularyRepository.saveAndFlush(po).toModel()
    }

    override fun findByPage(word: String?, study: Boolean?, pageRequest: PageRequest): Page<Vocabulary> {
        return vocabularyRepository.findByPage(word, study, pageRequest)
            .map { it.toModel() }
    }

    override fun findAll(): Collection<Vocabulary> {
        val pageable = PageRequest.ofSize(MAX_PAGE_SIZE).withSort(DEFAULT_SORT)
        return vocabularyRepository.findAllByDelIsFalse(pageable).map { it.toModel() }.content
    }

    override fun findByIdentity(identifier: VocabularyId): Optional<Vocabulary> {
        return vocabularyRepository.findById(identifier).map { it.toModel() }
    }
}
