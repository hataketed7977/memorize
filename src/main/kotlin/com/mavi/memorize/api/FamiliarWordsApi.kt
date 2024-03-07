package com.mavi.memorize.api

import com.mavi.memorize.data.entity.FamiliarWord

interface FamiliarWordsApi {
    fun count(): Long
    fun addFamiliarWord(vocabularyId: String): FamiliarWord
    fun deleteByVocabularyId(id: String)
}
