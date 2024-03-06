package com.mavi.memorize.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity
@Table(name = "t_vocabularies")
class Vocabulary {
    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.of("Asia/Shanghai"))
    }

    @Id
    lateinit var id: String
    lateinit var word: String
    lateinit var meaning: String
    lateinit var pron: String

    @Column(name = "part_of_speech")
    lateinit var partOfSpeech: String

    var study: Boolean = false
    var del: Boolean = false
    var sentence: String? = null

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    fun displayCreatedAt(): String = formatter.format(createdAt)
    fun displayStudy(): String = if (study) "Yes" else "No"
}
