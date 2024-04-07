package com.mavi.memorize.data

import com.mavi.memorize.DBTest
import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.data.entity.FamiliarWord
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.data.repository.VocabularyRepository
import com.mavi.memorize.helper.addStudiedVocabulary
import com.mavi.memorize.helper.addVocabularyRequest
import com.mavi.memorize.helper.initFamiliarWord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

@DBTest
class FamiliarWordsApiImplTest {
    @Autowired
    private lateinit var familiarWordsApi: FamiliarWordsApi

    @Autowired
    private lateinit var vocabularyApi: VocabulariesApi

    lateinit var vocabulary: Vocabulary
    lateinit var familiarWord: FamiliarWord

    @BeforeEach
    fun setup() {
        vocabulary = vocabularyApi.addStudiedVocabulary(addVocabularyRequest(word = "f1"))
        familiarWord = familiarWordsApi.initFamiliarWord(vocabulary.id, 1, 1)
    }

    @Test
    fun `should not add new familiar word if word not exist in vocabulary`() {
        val addFamiliarWord = familiarWordsApi.addFamiliarWord("invalid-id-1", false)
        assertThat(addFamiliarWord).isNull()
    }

    @Test
    fun `should update round`() {
        val addFamiliarWord = familiarWordsApi.addFamiliarWord(vocabulary.id, false)
        assertThat(addFamiliarWord?.round1).isNotNull()
        assertThat(addFamiliarWord?.round2).isNotNull()
        assertThat(addFamiliarWord?.round3).isNull()
        assertThat(addFamiliarWord?.round4).isNull()
        assertThat(addFamiliarWord?.createdAt).isEqualTo(familiarWord.createdAt)
    }

    @Test
    fun `should clean round`() {
        val addFamiliarWord = familiarWordsApi.addFamiliarWord(vocabulary.id, true)
        assertThat(addFamiliarWord?.round1).isNull()
        assertThat(addFamiliarWord?.round2).isNull()
        assertThat(addFamiliarWord?.round3).isNull()
        assertThat(addFamiliarWord?.round4).isNull()
        assertThat(addFamiliarWord?.createdAt).isNotEqualTo(familiarWord.createdAt)
    }

    @Test
    fun `should add new familiar word`() {
        val newVocabulary = vocabularyApi.addStudiedVocabulary(addVocabularyRequest(word = "f2"))
        val addFamiliarWord = familiarWordsApi.addFamiliarWord(newVocabulary.id, false)
        assertThat(addFamiliarWord?.round1).isNull()
    }
}
