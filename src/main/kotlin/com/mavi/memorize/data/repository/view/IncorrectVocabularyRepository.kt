package com.mavi.memorize.data.repository.view

import com.mavi.memorize.data.entity.view.IncorrectVocabulary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface IncorrectVocabularyRepository : ListPagingAndSortingRepository<IncorrectVocabulary, String> {
    fun findAllByCountGreaterThan(count: Int, pageable: Pageable): Page<IncorrectVocabulary>
}
