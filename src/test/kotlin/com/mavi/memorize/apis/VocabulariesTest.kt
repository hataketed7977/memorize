package com.mavi.memorize.apis

import com.mavi.memorize.DBTest
import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.api.IncorrectWordsApi
import com.mavi.memorize.api.UnfamiliarWordsApi
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.helper.addVocabularyRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DBTest
class VocabulariesTest {
    @Autowired
    private lateinit var vocabulariesApi: VocabulariesApi

    @Autowired
    private lateinit var unfamiliarWordsApi: UnfamiliarWordsApi

    @Autowired
    private lateinit var familiarWordsApi: FamiliarWordsApi

    @Autowired
    private lateinit var incorrectWordsApi: IncorrectWordsApi

    @Test
    fun `should remove vocabulary`() {
        val vocabulary = vocabulariesApi.addVocabulary(addVocabularyRequest())
        vocabulariesApi.removeVocabularyById(vocabulary.id)

        assertThat(vocabulariesApi.findById(vocabulary.id)).isNotPresent
        assertRelatedWordsAreNotPresent(vocabulary.id)
    }

    @Test
    fun `should remove all words when remove vocabulary`() {
        val vocabulary = vocabulariesApi.addVocabulary(addVocabularyRequest())
        unfamiliarWordsApi.batchCreateUnfamiliarWords(1)
        familiarWordsApi.addFamiliarWord(vocabulary.id, false)
        incorrectWordsApi.addIncorrectWord(vocabulary.id)
        assertRelatedWordsArePresent(vocabulary.id)

        vocabulariesApi.removeVocabularyById(vocabulary.id)

        assertThat(vocabulariesApi.findById(vocabulary.id)).isNotPresent
        assertRelatedWordsAreNotPresent(vocabulary.id)
    }

    private fun assertRelatedWordsAreNotPresent(vocabularyId: String) {
        assertThat(familiarWordsApi.findByVocabularyId(vocabularyId)).isNotPresent
        assertThat(unfamiliarWordsApi.findByVocabularyId(vocabularyId)).isNotPresent
        assertThat(incorrectWordsApi.findByVocabularyId(vocabularyId)).isNotPresent
    }

    private fun assertRelatedWordsArePresent(vocabularyId: String) {
        assertThat(familiarWordsApi.findByVocabularyId(vocabularyId)).isPresent
        assertThat(unfamiliarWordsApi.findByVocabularyId(vocabularyId)).isPresent
        assertThat(incorrectWordsApi.findByVocabularyId(vocabularyId)).isPresent
    }
}
