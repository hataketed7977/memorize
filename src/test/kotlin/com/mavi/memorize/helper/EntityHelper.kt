package com.mavi.memorize.helper

import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.entity.Vocabulary
import java.time.Instant
import java.util.*

fun addVocabularyRequest(
    word: String = UUID.randomUUID().toString(),
    meaning: String = "test meaning",
    pron: String = "[a]",
    partOfSpeech: String = "n.",
    sentence: String? = null
) = AddVocabularyRequest(
    word = word,
    meaning = meaning,
    pron = pron,
    partOfSpeech = partOfSpeech,
    sentence = sentence
)

fun vocabulary(
    id: String,
    word: String = UUID.randomUUID().toString(),
    meaning: String = "test meaning",
    pron: String = "[a]",
    partOfSpeech: String = "n.",
    sentence: String? = null,
    study: Boolean = false,
    createdAt: Instant = Instant.now(),
    del: Boolean = false
): Vocabulary {
    val vocabulary = Vocabulary()
    vocabulary.id = id
    vocabulary.word = word
    vocabulary.meaning = meaning
    vocabulary.pron = pron
    vocabulary.partOfSpeech = partOfSpeech
    vocabulary.study = study
    vocabulary.sentence = sentence
    vocabulary.del = del
    vocabulary.createdAt = createdAt
    return vocabulary
}
