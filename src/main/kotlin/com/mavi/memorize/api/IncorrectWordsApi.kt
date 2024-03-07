package com.mavi.memorize.api

import com.mavi.memorize.data.entity.IncorrectWord
import java.util.*

interface IncorrectWordsApi {
    fun count(): Long
    fun addIncorrectWord(vocabularyId: String): IncorrectWord
    fun deleteByVocabularyId(id: String)
    fun findAllByCountGreaterThanZero(): List<IncorrectWord>
    fun reduceCountByVocabularyId(vocabularyId: String)
    fun findByVocabularyId(vocabularyId: String): Optional<IncorrectWord>
}
