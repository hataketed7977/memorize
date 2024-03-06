package com.mavi.memorize.data.repository

import com.mavi.memorize.data.entity.UnfamiliarWord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UnfamiliarWordRepository : JpaRepository<UnfamiliarWord, String>
