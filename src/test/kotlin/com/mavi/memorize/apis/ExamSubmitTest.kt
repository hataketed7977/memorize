package com.mavi.memorize.apis

import com.mavi.memorize.DBTest
import com.mavi.memorize.api.*
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.helper.initVocabulary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DBTest
class ExamSubmitTest {
    @Autowired
    private lateinit var vocabulariesApi: VocabulariesApi

    @Autowired
    private lateinit var unfamiliarWordsApi: UnfamiliarWordsApi

    @Autowired
    private lateinit var familiarWordsApi: FamiliarWordsApi

    @Autowired
    private lateinit var incorrectWordsApi: IncorrectWordsApi

    private lateinit var vocabularies: List<Vocabulary>

    @BeforeEach
    fun setup() {
        vocabularies = vocabulariesApi.initVocabulary(20, 3, 2)
        unfamiliarWordsApi.batchCreateUnfamiliarWords(100)
//        familiarWordsApi.initFullFamiliarWords(vocabularies.map { it.id })
    }

    @Test
    fun `Nothing will happened when filled is empty`() {
        val filled: Map<VocabularyId, FilledValue> = mutableMapOf()
        vocabulariesApi.checkExamVocabularies(filled)

        assertThat(unfamiliarWordsApi.count()).isEqualTo(20)
        assertThat(familiarWordsApi.count()).isZero()
        assertThat(incorrectWordsApi.count()).isZero()
    }

    @Test
    fun `should remove unfamiliar when filled right`() {
        val filled: Map<VocabularyId, FilledValue> = vocabularies.associate { it.id to it.word }
        vocabulariesApi.checkExamVocabularies(filled)

        assertThat(unfamiliarWordsApi.count()).isEqualTo(0)
    }

    @Test
    fun `should remove unfamiliar when filled wrong`() {
        val filled: Map<VocabularyId, FilledValue> = vocabularies.associate { it.id to "anyway" }
        vocabulariesApi.checkExamVocabularies(filled)

        assertThat(unfamiliarWordsApi.count()).isEqualTo(0)
    }

    fun `should mark vocabulary as study when filled`() {

    }

    fun `should add to familiar words when filled right`() {

    }

    //round update

    fun `should reduce incorrect word error count when filled right`() {

    }

    fun `should add to incorrect words when filled wrong`() {

    }

    //add exam record
}
