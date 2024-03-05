package com.mavi.memorize.common.archtype.association

import com.mavi.memorize.common.archtype.model.Entity


fun interface HasOne<E : Entity<*, *>> : Association {
    fun get(): E
}
