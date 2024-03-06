package com.mavi.memorize.data.repository

import com.mavi.memorize.data.entity.Vocabulary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface VocabularyRepository : JpaRepository<Vocabulary, String> {
    @Query("SELECT v FROM Vocabulary v WHERE v.del = false and (:word is null or v.word LIKE %:word%) and (:study is null or v.study = :study)")
    fun findByPage(
        @Param("word") word: String?,
        @Param("study") study: Boolean?,
        pageable: Pageable
    ): Page<Vocabulary>

    fun findAllByDelIsFalseAndStudyIsFalseOrderByCreatedAt(pageable: Pageable): List<Vocabulary>
}
