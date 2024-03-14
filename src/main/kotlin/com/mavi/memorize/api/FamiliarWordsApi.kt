package com.mavi.memorize.api

import com.mavi.memorize.data.entity.FamiliarWord
import com.mavi.memorize.data.entity.view.FamiliarVocabulary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface FamiliarWordsApi {
    fun count(): Long
    fun addFamiliarWord(vocabularyId: String): FamiliarWord?
    fun deleteByVocabularyId(id: String)
    fun findAllNotStartRounds(): List<FamiliarWord>
    fun findByVocabularyId(vocabularyId: String): Optional<FamiliarWord>
    fun update(familiarWord: FamiliarWord): FamiliarWord
    fun updateRoundByVocabularyId(vocabularyId: String)
    fun findFamiliarVocabularies(pageable: Pageable): Page<FamiliarVocabulary>
    fun removeByVocabularyId(vocabularyId: String)
}
