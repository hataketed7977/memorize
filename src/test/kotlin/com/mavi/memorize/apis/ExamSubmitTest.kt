package com.mavi.memorize.apis

import com.mavi.memorize.DBTest
import com.mavi.memorize.api.*
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.helper.initFullFamiliarWords
import com.mavi.memorize.helper.initVocabulary
import com.mavi.memorize.helper.markAsStudy
import com.mavi.memorize.ui.components.toLocalDateMap
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import java.time.LocalDate

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

    @Autowired
    private lateinit var memorizeRecordsApi: MemorizeRecordsApi


    private lateinit var vocabularies: List<Vocabulary>

    @BeforeEach
    fun setup() {
        vocabularies = vocabulariesApi.initVocabulary(20, 3, 2)
    }

    @Test
    fun `Nothing will happened when filled is empty`() {
        unfamiliarWordsApi.batchCreateUnfamiliarWords(100)
        val filled: Map<VocabularyId, FilledValue> = mutableMapOf()
        vocabulariesApi.checkExamVocabularies(filled)

        assertThat(unfamiliarWordsApi.count()).isEqualTo(20)
        assertThat(familiarWordsApi.count()).isZero()
        assertThat(incorrectWordsApi.count()).isZero()
    }

    @Test
    fun `should remove unfamiliar when filled right`() {
        unfamiliarWordsApi.batchCreateUnfamiliarWords(100)
        val filled: Map<VocabularyId, FilledValue> = vocabularies.associate { it.id to it.word }
        vocabulariesApi.checkExamVocabularies(filled)

        assertThat(unfamiliarWordsApi.count()).isEqualTo(0)
    }

    @Test
    fun `should remove unfamiliar when filled wrong`() {
        unfamiliarWordsApi.batchCreateUnfamiliarWords(100)
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

    @Test
    fun `should update round time when is a familiar words review exam`() {
        val ids = vocabularies.map { it.id }
        familiarWordsApi.initFullFamiliarWords(ids)
        vocabulariesApi.markAsStudy(*ids.subList(0, 16).toTypedArray())
        unfamiliarWordsApi.batchCreateUnfamiliarWords(100)
        val examIds = vocabulariesApi.findExamVocabularies()
        val rightFilled = examIds.subList(0, 6).associate { it.id to it.word }
        val wrongFilled = examIds.subList(6, 12).associate { it.id to "anyway" }

        vocabulariesApi.checkExamVocabularies(rightFilled + wrongFilled)

        assertHasRound(ids[0], 0)
        assertHasRound(ids[1], 1)
        assertHasRound(ids[2], 1)

        assertHasRound(ids[3], 1)
        assertHasRound(ids[4], 2)
        assertHasRound(ids[5], 2)

        assertHasRound(ids[6], 2)
        assertHasRound(ids[7], 3)
        assertHasRound(ids[8], 3)

        assertHasRound(ids[9], 3)
        assertHasRound(ids[10], 4)
        assertHasRound(ids[11], 4)

        assertHasRound(ids[12], 4)
        assertHasRound(ids[13], 4)
        assertHasRound(ids[14], 4)
        assertHasRound(ids[15], 4)
    }

    @Test
    fun `should record memorize words`() {
        val date = LocalDate.of(2024, 3, 11)
        mockkStatic(LocalDate::class)
        every { LocalDate.now() } returns date
        val rightFilled = vocabularies.subList(0, 10).associate { it.id to it.word }
        val wrongFilled = vocabularies.subList(10, 20).associate { it.id to "anyway" }
        vocabulariesApi.checkExamVocabularies(rightFilled + wrongFilled)
        unmockkStatic(LocalDate::class)

        assertThat(memorizeRecordsApi.findByMonth(2024, 3).toLocalDateMap())
            .isEqualTo(
                mapOf<LocalDate, List<String>>(
                    date to rightFilled.values.toList()
                )
            )

        assertThat(memorizeRecordsApi.findByMonth(2024, 2)).isEmpty()
        assertThat(memorizeRecordsApi.findByMonth(2024, 4)).isEmpty()
    }

    private fun assertHasRound(vocabularyId: String, round: Int) {
        val familiarWord = familiarWordsApi.findByVocabularyId(vocabularyId).get()
        when (round) {
            1 -> assertThat(familiarWord.round1 != null && familiarWord.round2 == null && familiarWord.round3 == null && familiarWord.round4 == null).isTrue()
            2 -> assertThat(familiarWord.round1 != null && familiarWord.round2 != null && familiarWord.round3 == null && familiarWord.round4 == null).isTrue()
            3 -> assertThat(familiarWord.round1 != null && familiarWord.round2 != null && familiarWord.round3 != null && familiarWord.round4 == null).isTrue()
            4 -> assertThat(familiarWord.round1 != null && familiarWord.round2 != null && familiarWord.round3 != null && familiarWord.round4 != null).isTrue()
            else -> assertThat(familiarWord.round1 == null && familiarWord.round2 == null && familiarWord.round3 == null && familiarWord.round4 == null).isTrue()
        }
    }
}
