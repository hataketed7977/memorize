package com.mavi.memorize.common.archtype.association

import com.mavi.memorize.common.archtype.model.Entity


interface HasMany<ID, E : Entity<ID, *>> : Association {
    fun findAll(): Collection<E>
    fun findByIdentity(identifier: ID): E
}
