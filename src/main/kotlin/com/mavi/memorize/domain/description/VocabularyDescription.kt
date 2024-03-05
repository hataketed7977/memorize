package com.mavi.memorize.domain.description

import java.time.Instant

data class VocabularyDescription(
    val word: String,
    val meaning: String,
    val pron: String,
    val partOfSpeech: String,
    val study: Boolean = false,
    val createdAt: Instant = Instant.now()
)
