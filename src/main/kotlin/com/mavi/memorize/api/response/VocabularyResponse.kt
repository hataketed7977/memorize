package com.mavi.memorize.api.response

import com.mavi.memorize.domain.model.Vocabulary
import java.time.Instant

data class VocabularyResponse(
    val id: String,
    val word: String,
    val meaning: String,
    val pron: String,
    val partOfSpeech: String,
    val study: Boolean,
    val createdAt: Instant
)

fun Vocabulary.toRepresentation(): VocabularyResponse {
    return VocabularyResponse(
        id = identity,
        word = description.word,
        meaning = description.meaning,
        pron = description.pron,
        partOfSpeech = description.partOfSpeech,
        study = description.study,
        createdAt = description.createdAt
    )
}
