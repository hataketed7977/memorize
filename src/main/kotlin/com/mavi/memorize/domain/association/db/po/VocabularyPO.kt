package com.mavi.memorize.domain.association.db.po

import com.mavi.memorize.domain.description.VocabularyDescription
import com.mavi.memorize.domain.model.Vocabulary
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "t_vocabularies")
class VocabularyPO {
    @Id
    lateinit var id: String
    lateinit var word: String
    lateinit var meaning: String
    lateinit var pron: String

    @Column(name = "part_of_speech")
    lateinit var partOfSpeech: String

    var study: Boolean = false
    var del: Boolean = false

    @Column(name = "created_at")
    lateinit var createdAt: Instant
}

fun VocabularyPO.toModel(): Vocabulary {
    return Vocabulary(
        identity = id,
        description = VocabularyDescription(
            word = word,
            meaning = meaning,
            pron = pron,
            partOfSpeech = partOfSpeech,
            study = study,
            createdAt = createdAt
        )
    )
}
