package com.mavi.memorize.apis

import com.mavi.memorize.DBTest
import com.mavi.memorize.api.*
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.helper.initVocabulary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable

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

    @Test
    fun `should mark vocabulary as study when filled`() {
        val rightFilled = vocabularies.subList(0, 10).associate { it.id to it.word }
        val wrongFilled = vocabularies.subList(10, 20).associate { it.id to "anyway" }
        vocabulariesApi.checkExamVocabularies(rightFilled + wrongFilled)

        assertThat(vocabulariesApi.count().second).isEqualTo(23)
        assertThat(vocabulariesApi.count().third).isEqualTo(0)
        assertThat(vocabulariesApi.findAllByIds((rightFilled + wrongFilled).map { it.key }, Pageable.ofSize(50))
            .map { it.study }.all { it }).isTrue()

    }

    @Test
    fun `should add to familiar words when filled right`() {
        val rightFilled = vocabularies.subList(0, 10).associate { it.id to it.word }
        val wrongFilled = vocabularies.subList(10, 20).associate { it.id to "anyway" }
        vocabulariesApi.checkExamVocabularies(rightFilled + wrongFilled)

        val vocabularyIds = familiarWordsApi.findAllNotStartRounds().map { it.vocabularyId }
        assertThat(vocabularyIds).containsAnyElementsOf(rightFilled.map { it.key })
        assertThat(vocabularyIds).doesNotContainAnyElementsOf(wrongFilled.map { it.key })
    }

    @Test
    fun `should add to incorrect words when filled wrong`() {
        val rightFilled = vocabularies.subList(0, 10).associate { it.id to it.word }
        val wrongFilled = vocabularies.subList(10, 20).associate { it.id to "anyway" }
        vocabulariesApi.checkExamVocabularies(rightFilled + wrongFilled)

        val vocabularyIds = incorrectWordsApi.findAllByCountGreaterThanZero().map { it.vocabularyId }
        assertThat(vocabularyIds).containsAnyElementsOf(wrongFilled.map { it.key })
        assertThat(vocabularyIds).doesNotContainAnyElementsOf(rightFilled.map { it.key })
    }

    @Test
    fun `should reduce incorrect word error count when filled right`() {
        val rightFilled = vocabularies.subList(0, 10).associate { it.id to it.word }
        val wrongFilled = vocabularies.subList(10, 20).associate { it.id to "anyway" }
        val incorrectWords = rightFilled.map { it.key }.subList(0, 5).map {
            incorrectWordsApi.addIncorrectWord(it)
        }
        incorrectWords.forEach {
            assertThat(incorrectWordsApi.findByVocabularyId(it.vocabularyId).get().count).isOne()
        }

        vocabulariesApi.checkExamVocabularies(rightFilled + wrongFilled)

        val vocabularyIds = incorrectWordsApi.findAllByCountGreaterThanZero().map { it.vocabularyId }
        assertThat(vocabularyIds).containsAnyElementsOf(wrongFilled.map { it.key })
        assertThat(vocabularyIds).doesNotContainAnyElementsOf(rightFilled.map { it.key })
        incorrectWords.forEach {
            assertThat(incorrectWordsApi.findByVocabularyId(it.vocabularyId).get().count).isZero()
        }
    }

    //TODO round update
    //add exam record
}
