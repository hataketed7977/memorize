package com.mavi.memorize.data.repository

import com.mavi.memorize.data.entity.FamiliarWord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FamiliarWordRepository : JpaRepository<FamiliarWord, String> {
    fun findAllByRound1IsNotNull(): List<FamiliarWord>
    fun findAllByRound2IsNotNull(): List<FamiliarWord>
    fun findAllByRound3IsNotNull(): List<FamiliarWord>
    fun findAllByRound4IsNotNull(): List<FamiliarWord>

    fun deleteByVocabularyId(vocabularyId: String)
}
