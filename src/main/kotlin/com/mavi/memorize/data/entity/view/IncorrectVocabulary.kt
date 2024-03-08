package com.mavi.memorize.data.entity.view

import com.mavi.memorize.data.helper.DateHelper
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "v_incorrect_vocabularies")
class IncorrectVocabulary {
    @Id
    lateinit var id: String

    lateinit var word: String
    lateinit var meaning: String
    lateinit var pron: String

    @Column(name = "part_of_speech")
    lateinit var partOfSpeech: String

    @Column(name = "count")
    var count: Int = 1

    @Column(name = "incorrect_updated_at")
    lateinit var incorrectUpdatedAt: Instant

    fun displayUpdatedAt(): String = DateHelper.formatter.format(incorrectUpdatedAt)
}
