package com.mavi.memorize.domain.association.db.repository

import com.mavi.memorize.domain.association.db.po.VocabularyPO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VocabularyRepository : JpaRepository<VocabularyPO, String> {
    fun findAllByDelIsFalse(pageable: Pageable): Page<VocabularyPO>
    fun findAllByDelIsFalseAndStudy(study: Boolean, pageable: Pageable): Page<VocabularyPO>
}
