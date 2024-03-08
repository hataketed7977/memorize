package com.mavi.memorize.api

import com.mavi.memorize.data.entity.IncorrectWord
import com.mavi.memorize.data.entity.view.IncorrectVocabulary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IncorrectWordsApi {
    fun count(): Long
    fun addIncorrectWord(vocabularyId: String): IncorrectWord
    fun deleteByVocabularyId(id: String)
    fun findAllByCountGreaterThanZero(): List<IncorrectWord>
    fun reduceCountByVocabularyId(vocabularyId: String)
    fun findByVocabularyId(vocabularyId: String): Optional<IncorrectWord>
    fun findIncorrectVocabularies(pageable: Pageable): Page<IncorrectVocabulary>

}
