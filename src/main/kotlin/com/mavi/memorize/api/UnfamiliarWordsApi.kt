package com.mavi.memorize.api

import com.mavi.memorize.data.entity.UnfamiliarWord
import java.util.*

interface UnfamiliarWordsApi {
    fun count(): Long
    fun batchCreateUnfamiliarWords(number: Int): List<UnfamiliarWord>
    fun findAll(): List<UnfamiliarWord>
    fun findByVocabularyId(vocabularyId: String): Optional<UnfamiliarWord>
    fun deleteByVocabularyId(id: String)
}
