package com.mavi.memorize.api

import com.mavi.memorize.data.entity.UnfamiliarWord

interface UnfamiliarWordsApi {
    fun count(): Long
    fun batchCreateUnfamiliarWords(number: Int): List<UnfamiliarWord>
    fun findAll(): List<UnfamiliarWord>
}
