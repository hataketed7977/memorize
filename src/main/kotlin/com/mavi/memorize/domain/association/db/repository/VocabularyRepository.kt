package com.mavi.memorize.domain.association.db.repository

import com.mavi.memorize.domain.association.db.po.VocabularyPO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface VocabularyRepository : JpaRepository<VocabularyPO, String> {
    fun findAllByDelIsFalse(pageable: Pageable): Page<VocabularyPO>

    @Query("SELECT v FROM VocabularyPO v WHERE (:word is null or v.word LIKE %:word%) and (:study is null or v.study = :study)")
    fun findByPage(
        @Param("word") word: String?,
        @Param("study") study: Boolean?, pageable: Pageable
    ): Page<VocabularyPO>
}
