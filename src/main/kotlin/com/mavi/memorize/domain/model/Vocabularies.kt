package com.mavi.memorize.domain.model

import com.mavi.memorize.common.archtype.association.HasMany
import com.mavi.memorize.domain.description.VocabularyDescription
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface Vocabularies : HasMany<VocabularyId, Vocabulary> {
    fun addVocabulary(description: VocabularyDescription): Vocabulary
    fun findByPage(word: String?, study: Boolean?, pageRequest: PageRequest): Page<Vocabulary>
}
