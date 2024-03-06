package com.mavi.memorize.data.repository

import com.mavi.memorize.data.entity.FamiliarWord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FamiliarWordRepository : JpaRepository<FamiliarWord, String> {
    fun deleteByVocabularyId(vocabularyId: String)
}
