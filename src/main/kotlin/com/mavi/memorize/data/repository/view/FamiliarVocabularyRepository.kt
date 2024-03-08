package com.mavi.memorize.data.repository.view

import com.mavi.memorize.data.entity.view.FamiliarVocabulary
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface FamiliarVocabularyRepository : ListPagingAndSortingRepository<FamiliarVocabulary, String>
