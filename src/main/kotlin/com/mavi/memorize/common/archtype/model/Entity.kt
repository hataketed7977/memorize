package com.mavi.memorize.common.archtype.model

interface Entity<Identity, Description> {
    val identity: Identity?
    val description: Description?
}
