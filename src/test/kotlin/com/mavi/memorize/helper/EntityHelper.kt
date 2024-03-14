package com.mavi.memorize.helper

import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.data.MemorizeRecord
import com.mavi.memorize.data.entity.view.FamiliarVocabulary
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
): MemorizeRecord = MemorizeRecord(
    year = year,
    month = month,
    day = day,
    words = words.map {
        val item = FamiliarVocabulary()
        item.word = it
        item.partOfSpeech = "n."
        item.meaning = "test meaning"
        item
    }
)
