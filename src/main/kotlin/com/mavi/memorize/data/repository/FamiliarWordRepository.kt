package com.mavi.memorize.data.repository

import com.mavi.memorize.data.entity.FamiliarWord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FamiliarWordRepository : JpaRepository<FamiliarWord, String> {
    fun findByVocabularyId(vocabularyId: String): Optional<FamiliarWord>
    fun findAllByRound4IsNull(): List<FamiliarWord>
    fun deleteByVocabularyId(vocabularyId: String)
}
