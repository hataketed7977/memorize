package com.mavi.memorize.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "t_incorrect_words")
class IncorrectWord {
    @Id
    lateinit var id: String

    @Column(name = "vocabulary_id")
    lateinit var vocabularyId: String

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    @Column(name = "updated_at")
    lateinit var updatedAt: Instant

    var count: Int = 1
}
