package com.mavi.memorize.data

import com.mavi.memorize.DBTest
import com.mavi.memorize.api.IncorrectWordsApi
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.helper.addStudiedVocabulary
import com.mavi.memorize.helper.addVocabularyRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DBTest
class IncorrectWordsApiImplTest {
    @Autowired
    private lateinit var vocabularyApi: VocabulariesApi
    @Autowired
    lateinit var incorrectWordsApi: IncorrectWordsApi

    lateinit var vocabulary: Vocabulary

    @BeforeEach
    fun setup() {
        vocabulary = vocabularyApi.addStudiedVocabulary(addVocabularyRequest())
        incorrectWordsApi.addIncorrectWord(vocabulary.id)
    }

    @Test
    fun `should return true when reduce count`() {
        val hasReduce = incorrectWordsApi.reduceCountByVocabularyId(vocabulary.id)
        assertTrue(hasReduce)
    }

    @Test
    fun `should return false when not reduce count`() {
        val hasReduce = incorrectWordsApi.reduceCountByVocabularyId("incorrect-2")
        assertFalse(hasReduce)
    }
}
