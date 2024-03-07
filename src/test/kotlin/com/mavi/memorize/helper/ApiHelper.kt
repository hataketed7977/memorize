package com.mavi.memorize.helper

import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.entity.Vocabulary

fun VocabulariesApi.addStudiedVocabulary(request: AddVocabularyRequest): Vocabulary {
    val vocabulary = addVocabulary(request)
    vocabulary.study = true
    updateVocabulary(vocabulary)
    return vocabulary
}

fun VocabulariesApi.addDeletedVocabulary(request: AddVocabularyRequest): Vocabulary {
    val vocabulary = addVocabulary(request)
    vocabulary.del = true
    updateVocabulary(vocabulary)
    return vocabulary
}
