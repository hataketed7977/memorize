package com.mavi.memorize.api

import com.mavi.memorize.data.entity.IncorrectWord

interface IncorrectWordsApi {
    fun count(): Long
    fun addIncorrectWord(vocabularyId: String): IncorrectWord
    fun deleteByVocabularyId(id: String)
}
