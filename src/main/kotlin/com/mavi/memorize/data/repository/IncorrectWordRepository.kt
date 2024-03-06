package com.mavi.memorize.data.repository

import com.mavi.memorize.data.entity.IncorrectWord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IncorrectWordRepository : JpaRepository<IncorrectWord, String> {
    fun findByVocabularyId(vocabularyId: String): Optional<IncorrectWord>
    fun deleteByVocabularyId(vocabularyId: String)
}
