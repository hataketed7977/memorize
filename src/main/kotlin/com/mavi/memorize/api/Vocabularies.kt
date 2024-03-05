package com.mavi.memorize.api

import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.entity.Vocabulary
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface Vocabularies {
    fun addVocabulary(request: AddVocabularyRequest): Vocabulary
    fun findByPage(word: String?, study: Boolean?, pageRequest: PageRequest): Page<Vocabulary>
}
