package com.mavi.memorize.data.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "t_memorize_records")
class MemorizeRecord {
    @Id
    lateinit var id: String

    @Column(name = "words")
    @Convert(converter = WordsConverter::class)
    lateinit var words: List<String>

    @Column(name = "created_at")
    lateinit var createdAt: Instant

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
}

@Converter(autoApply = true)
class WordsConverter : AttributeConverter<List<String>, String> {
    companion object {
        val objectMapper = jacksonObjectMapper()
    }

    override fun convertToDatabaseColumn(words: List<String>): String {
        return objectMapper.writeValueAsString(words)
    }

    override fun convertToEntityAttribute(wordsJson: String): List<String> {
        return objectMapper.readValue<List<String>>(wordsJson)
    }
}
