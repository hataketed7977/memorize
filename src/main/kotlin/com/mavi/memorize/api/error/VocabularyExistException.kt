package com.mavi.memorize.api.error

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class VocabularyExistException(word: String) : ResponseStatusException(
    HttpStatus.BAD_REQUEST, "Vocabulary [$word] has exist."
)
