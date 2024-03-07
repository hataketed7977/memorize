package com.mavi.memorize.data.repository

import com.mavi.memorize.data.entity.UnfamiliarWord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UnfamiliarWordRepository : JpaRepository<UnfamiliarWord, String> {
    fun findByVocabularyId(vocabularyId: String): Optional<UnfamiliarWord>
    fun deleteByVocabularyId(vocabularyId: String)
}
