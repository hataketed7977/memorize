package com.mavi.memorize.data

import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.data.repository.VocabularyRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class VocabulariesApiImpl(
    private val vocabularyRepository: VocabularyRepository
) : VocabulariesApi {
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
}
