package com.mavi.memorize.apis

import com.mavi.memorize.DBTest
import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.api.IncorrectWordsApi
import com.mavi.memorize.api.UnfamiliarWordsApi
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.helper.initFamiliarWord
import com.mavi.memorize.helper.initVocabulary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import java.time.Instant
import java.time.temporal.ChronoUnit

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
        //Review cycle: 2,4,7,20
        //Not Review
        familiarWordsApi.initFamiliarWord(ids[0], 1)
        familiarWordsApi.initFamiliarWord(ids[1], 2)
        familiarWordsApi.initFamiliarWord(ids[2], 3)
        //Review 1 round
        familiarWordsApi.initFamiliarWord(ids[3], 3, 1)
        familiarWordsApi.initFamiliarWord(ids[4], 4, 1)
        familiarWordsApi.initFamiliarWord(ids[5], 5, 1)
        //Review 2 round
        familiarWordsApi.initFamiliarWord(ids[6], 5, 2)
        familiarWordsApi.initFamiliarWord(ids[7], 7, 2)
        familiarWordsApi.initFamiliarWord(ids[8], 15, 2)
        //Review 3 round
        familiarWordsApi.initFamiliarWord(ids[9], 15, 3)
        familiarWordsApi.initFamiliarWord(ids[10], 20, 3)
        familiarWordsApi.initFamiliarWord(ids[11], 22, 3)
        //Review 4 round
        familiarWordsApi.initFamiliarWord(ids[12], 3, 4)
        familiarWordsApi.initFamiliarWord(ids[13], 5, 4)
        familiarWordsApi.initFamiliarWord(ids[14], 8, 4)
        familiarWordsApi.initFamiliarWord(ids[15], 25, 4)

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
