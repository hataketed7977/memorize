package com.mavi.memorize.helper

import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.entity.FamiliarWord
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
    removeVocabularyById(vocabulary.id)
    return vocabulary
}

fun VocabulariesApi.markAsStudy(vararg ids: String) {
    ids.forEach {
        val vocabulary = findById(it).get()
        vocabulary.study = true
        updateVocabulary(vocabulary)
    }
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

fun FamiliarWordsApi.initFamiliarWord(vocabularyId: String, beforeDays: Long, round: Int = 0): FamiliarWord {
    val familiarWord = addFamiliarWord(vocabularyId, false)
    familiarWord!!.createdAt = Instant.now().minus(beforeDays, ChronoUnit.DAYS)
    if (round >= 1) familiarWord.round1 = Instant.now()
    if (round >= 2) familiarWord.round2 = Instant.now()
    if (round >= 3) familiarWord.round3 = Instant.now()
    if (round >= 4) familiarWord.round4 = Instant.now()
    update(familiarWord)
    return familiarWord
}

fun FamiliarWordsApi.initFullFamiliarWords(vocabularyId: List<String>) {
    //Review cycle: 2,4,7,20
    //Not Review
    initFamiliarWord(vocabularyId[0], 1)
    initFamiliarWord(vocabularyId[1], 2)
    initFamiliarWord(vocabularyId[2], 3)
    //Review 1 round
    initFamiliarWord(vocabularyId[3], 3, 1)
    initFamiliarWord(vocabularyId[4], 4, 1)
    initFamiliarWord(vocabularyId[5], 5, 1)
    //Review 2 round
    initFamiliarWord(vocabularyId[6], 5, 2)
    initFamiliarWord(vocabularyId[7], 7, 2)
    initFamiliarWord(vocabularyId[8], 15, 2)
    //Review 3 round
    initFamiliarWord(vocabularyId[9], 15, 3)
    initFamiliarWord(vocabularyId[10], 20, 3)
    initFamiliarWord(vocabularyId[11], 22, 3)
    //Review 4 round
    initFamiliarWord(vocabularyId[12], 3, 4)
    initFamiliarWord(vocabularyId[13], 5, 4)
    initFamiliarWord(vocabularyId[14], 8, 4)
    initFamiliarWord(vocabularyId[15], 25, 4)
}
