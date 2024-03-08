package com.mavi.memorize.apis

import com.mavi.memorize.DBTest
import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.api.IncorrectWordsApi
import com.mavi.memorize.api.UnfamiliarWordsApi
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.helper.initFullFamiliarWords
import com.mavi.memorize.helper.initVocabulary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest

@DBTest
class ExamBuildTest {
    @Autowired
    private lateinit var vocabulariesApi: VocabulariesApi

    @Autowired
    private lateinit var unfamiliarWordsApi: UnfamiliarWordsApi

    @Autowired
    private lateinit var familiarWordsApi: FamiliarWordsApi

    @Autowired
    private lateinit var incorrectWordsApi: IncorrectWordsApi

    @BeforeEach
    fun setup() {
        vocabulariesApi.initVocabulary(20, 3, 2)
    }

    @Test
    fun `should test unfamiliar words`() {
        val ids = unfamiliarWordsApi.batchCreateUnfamiliarWords(30).map { it.vocabularyId }

        val vocabularies = vocabulariesApi.findExamVocabularies().map { it.id }
        assertThat(vocabularies.size).isEqualTo(20)
        assertThat(vocabularies).containsExactlyInAnyOrderElementsOf(ids)
    }

    @Test
    fun `should test incorrect words when error count greater than 0`() {
        val ids = vocabulariesApi.findByPage(null, null, PageRequest.ofSize(100))
            .map { it.id }.content
        incorrectWordsApi.addIncorrectWord(ids[0])
        incorrectWordsApi.addIncorrectWord(ids[1])
        incorrectWordsApi.reduceCountByVocabularyId(ids[1])

        val vocabularies = vocabulariesApi.findExamVocabularies().map { it.id }
        assertThat(vocabularies.size).isEqualTo(1)
        assertThat(vocabularies).containsExactly(ids[0])
    }

    @Test
    fun `should test familiar words by rounds`() {
        val ids = vocabulariesApi.findByPage(null, null, PageRequest.ofSize(100))
            .map { it.id }.content
        familiarWordsApi.initFullFamiliarWords(ids)

        val vocabularies = vocabulariesApi.findExamVocabularies().map { it.id }

        assertThat(vocabularies.size).isEqualTo(8)
        assertThat(vocabularies).containsExactlyInAnyOrder(
            ids[1], ids[2],
            ids[4], ids[5],
            ids[7], ids[8],
            ids[10], ids[11],
        )
    }


}
