package com.mavi.memorize.helper

import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.entity.MemorizeRecord
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

fun memorizeRecord(
    words: List<String> = listOf("test"),
    year: Int = 2024,
    month: Int = 3,
    day: Int = 11,
): MemorizeRecord {
    val entity = MemorizeRecord()
    entity.id = UUID.randomUUID().toString()
    entity.words = words
    entity.createdAt = Instant.now()
    entity.year = year
    entity.month = month
    entity.day = day
    return entity
}
