package com.mavi.memorize.ui.model.error

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class VocabularyNotExistException(word: String) : ResponseStatusException(
    HttpStatus.BAD_REQUEST, "Vocabulary [$word] not exist."
)
