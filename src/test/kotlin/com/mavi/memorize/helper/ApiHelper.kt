package com.mavi.memorize.helper

import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.entity.Vocabulary
import java.time.Instant
import java.time.temporal.ChronoUnit

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

fun VocabulariesApi.initVocabulary(notStudy: Int, study: Int = 0, del: Int = 0): List<Vocabulary> {
    val vocabularies = mutableListOf<Vocabulary>()
    repeat(notStudy) {
        vocabularies.add(addVocabulary(addVocabularyRequest()))
    }

    repeat(study) {
        vocabularies.add(addStudiedVocabulary(addVocabularyRequest()))
    }

    repeat(del) {
        vocabularies.add(addDeletedVocabulary(addVocabularyRequest()))
    }
    return vocabularies
}

fun FamiliarWordsApi.initFamiliarWord(vocabularyId: String, beforeDays: Long, round: Int = 0) {
    val familiarWord = addFamiliarWord(vocabularyId)
    familiarWord.createdAt = Instant.now().minus(beforeDays, ChronoUnit.DAYS)
    if (round >= 1) familiarWord.round1 = Instant.now()
    if (round >= 2) familiarWord.round2 = Instant.now()
    if (round >= 3) familiarWord.round3 = Instant.now()
    if (round >= 4) familiarWord.round4 = Instant.now()
    update(familiarWord)
}
