package com.mavi.memorize.apis

import com.mavi.memorize.DBTest
import com.mavi.memorize.api.FamiliarWordsApi
import com.mavi.memorize.api.IncorrectWordsApi
import com.mavi.memorize.api.UnfamiliarWordsApi
import com.mavi.memorize.api.VocabulariesApi
import com.mavi.memorize.data.entity.Vocabulary
import com.mavi.memorize.helper.initVocabulary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DBTest
class WordsCountsTest {
    @Autowired
    private lateinit var vocabulariesApi: VocabulariesApi

    @Autowired
    private lateinit var unfamiliarWordsApi: UnfamiliarWordsApi

    @Autowired
    private lateinit var familiarWordsApi: FamiliarWordsApi

    @Autowired
    private lateinit var incorrectWordsApi: IncorrectWordsApi

    private fun initVocabulary(): List<Vocabulary> = vocabulariesApi.initVocabulary(5, 3, 2)

    @Test
    fun `should count vocabularies when data is empty`() {
        val counts = vocabulariesApi.count()
        assertThat(counts.first).isEqualTo(0)
        assertThat(counts.second).isEqualTo(0)
        assertThat(counts.third).isEqualTo(0)
    }

    @Test
    fun `should count vocabularies`() {
        initVocabulary()

        val counts = vocabulariesApi.count()
        assertThat(counts.first).isEqualTo(8)
        assertThat(counts.second).isEqualTo(3)
        assertThat(counts.third).isEqualTo(5)
    }

    @Test
    fun `should count unfamiliar words when data is empty `() {
        unfamiliarWordsApi.batchCreateUnfamiliarWords(100)
        val count = unfamiliarWordsApi.count()
        assertThat(count).isEqualTo(0)
    }

    @Test
    fun `should count unfamiliar words`() {
        initVocabulary()
        unfamiliarWordsApi.batchCreateUnfamiliarWords(100)
        unfamiliarWordsApi.batchCreateUnfamiliarWords(100)

        val count = unfamiliarWordsApi.count()
        assertThat(count).isEqualTo(5)
    }

    @Test
    fun `should count familiar words when data is empty`() {
        val count = familiarWordsApi.count()
        assertThat(count).isEqualTo(0)
    }

    @Test
    fun `should count familiar words`() {
        initVocabulary().forEach {
            familiarWordsApi.addFamiliarWord(it.id)
        }

        val count = familiarWordsApi.count()
        assertThat(count).isEqualTo(8)
    }

    @Test
    fun `should count incorrect words when data is empty`() {
        val count = incorrectWordsApi.count()
        assertThat(count).isEqualTo(0)
    }

    @Test
    fun `should count incorrect words`() {
        initVocabulary().forEach {
            incorrectWordsApi.addIncorrectWord(it.id)
        }

        val count = incorrectWordsApi.count()
        assertThat(count).isEqualTo(8)
    }
}
