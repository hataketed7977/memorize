package com.mavi.memorize.domain.model

import com.mavi.memorize.common.archtype.model.Entity
import com.mavi.memorize.domain.description.VocabularyDescription

typealias VocabularyId = String

class Vocabulary(
    override val identity: VocabularyId,
    override val description: VocabularyDescription
) : Entity<VocabularyId, VocabularyDescription>
