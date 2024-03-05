package com.mavi.memorize.ui.model.request

import com.mavi.memorize.domain.description.VocabularyDescription


data class AddVocabularyRequest(
    val word: String,
    val meaning: String,
    val pron: String,
    val partOfSpeech: String,
)

fun AddVocabularyRequest.toDescription(): VocabularyDescription {
    return VocabularyDescription(
        word = word,
        meaning = meaning,
        pron = pron,
        partOfSpeech = partOfSpeech
    )
}
