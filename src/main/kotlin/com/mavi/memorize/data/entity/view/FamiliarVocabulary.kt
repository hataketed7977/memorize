package com.mavi.memorize.data.entity.view

import com.mavi.memorize.data.helper.DateHelper
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "v_familiar_vocabularies")
class FamiliarVocabulary {
    @Id
    lateinit var id: String

    lateinit var word: String
    lateinit var meaning: String
    lateinit var pron: String

    @Column(name = "part_of_speech")
    lateinit var partOfSpeech: String

    @Column(name = "familiar_created_at")
    lateinit var familiarCreatedAt: Instant

    var round1: Instant? = null
    var round2: Instant? = null
    var round3: Instant? = null
    var round4: Instant? = null

    fun displayCreatedAt(): String = DateHelper.formatter.format(familiarCreatedAt)
}
