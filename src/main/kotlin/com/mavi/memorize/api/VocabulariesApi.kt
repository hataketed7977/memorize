package com.mavi.memorize.api

import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.entity.Vocabulary
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface VocabulariesApi {
    fun addVocabulary(request: AddVocabularyRequest): Vocabulary
    fun findByPage(word: String?, study: Boolean?, pageRequest: PageRequest): Page<Vocabulary>
    fun removeVocabularyById(id: String)
    fun updateVocabulary(item: Vocabulary): Vocabulary

    fun count(): Pair<Study, NotStudy>
}

typealias Study = Int
typealias NotStudy = Int
