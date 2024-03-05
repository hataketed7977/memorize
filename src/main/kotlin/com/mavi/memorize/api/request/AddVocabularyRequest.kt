package com.mavi.memorize.api.request


data class AddVocabularyRequest(
    val word: String,
    val meaning: String,
    val pron: String,
    val partOfSpeech: String,
    val sentence: String?,
)
