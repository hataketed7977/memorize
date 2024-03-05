package com.mavi.memorize.common.archtype.association

import com.mavi.memorize.common.archtype.model.Entity
import com.mavi.memorize.domain.model.Vocabulary
import java.util.*


interface HasMany<ID, E : Entity<ID, *>> : Association {
    fun findAll(): Collection<E>
    fun findByIdentity(identifier: ID): Optional<Vocabulary>
}
