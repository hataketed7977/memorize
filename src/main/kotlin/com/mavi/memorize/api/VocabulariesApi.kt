package com.mavi.memorize.api

import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.entity.Vocabulary
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

interface VocabulariesApi {
    fun findById(id: String): Optional<Vocabulary>
    fun addVocabulary(request: AddVocabularyRequest): Vocabulary
    fun findByPage(word: String?, study: Boolean?, pageRequest: PageRequest): Page<Vocabulary>
    fun removeVocabularyById(id: String)
    fun updateVocabulary(item: Vocabulary): Vocabulary
    fun count(): Triple<All, Study, NotStudy>
    fun findAllByIds(ids: List<String>, pageable: Pageable): Page<Vocabulary>
    fun findExamVocabularies(): List<Vocabulary>
    fun checkExamVocabularies(filled: Map<VocabularyId, FilledValue>)
}

typealias All = Long
typealias Study = Long
typealias NotStudy = Long

typealias VocabularyId = String
typealias FilledValue = String
