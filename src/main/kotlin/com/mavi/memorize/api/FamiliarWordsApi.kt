package com.mavi.memorize.api

import com.mavi.memorize.data.entity.FamiliarWord
import java.util.*

interface FamiliarWordsApi {
    fun count(): Long
    fun addFamiliarWord(vocabularyId: String): FamiliarWord
    fun deleteByVocabularyId(id: String)
    fun findByRound(round: Int): List<FamiliarWord>
    fun findByVocabularyId(vocabularyId: String): Optional<FamiliarWord>
}
