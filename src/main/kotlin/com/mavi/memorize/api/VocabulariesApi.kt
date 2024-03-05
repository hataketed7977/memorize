package com.mavi.memorize.api

import com.mavi.memorize.api.error.VocabularyExistException
import com.mavi.memorize.api.error.VocabularyNotExistException
import com.mavi.memorize.api.request.AddVocabularyRequest
import com.mavi.memorize.api.request.toDescription
import com.mavi.memorize.api.response.VocabularyResponse
import com.mavi.memorize.api.response.toRepresentation
import com.mavi.memorize.domain.model.Vocabularies
import com.mavi.memorize.domain.model.Vocabulary
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/apis/vocabularies")
class VocabulariesApi(
    private val vocabularies: Vocabularies
) {
    @PostMapping
    fun add(@RequestBody request: AddVocabularyRequest): VocabularyResponse {
        try {
            return vocabularies.addVocabulary(request.toDescription()).toRepresentation()
        } catch (e: DataIntegrityViolationException) {
            if (e.message?.contains("duplicate key") == true) {
                throw VocabularyExistException(request.word)
            }
            throw e
        }
    }

    @GetMapping
    fun findByPage(
        @RequestParam("pageNumber") pageNumber: Int,
        @RequestParam("pageSize") pageSize: Int,
        @RequestParam(name = "study", required = false) study: Boolean?
    ): Page<Vocabulary> {
        return vocabularies.findByPage(PageRequest.of(pageNumber, pageSize), study)
    }

    @GetMapping("{id}")
    fun findById(@PathVariable("id") id: String): VocabularyResponse {
        return vocabularies.findByIdentity(id).map { it.toRepresentation() }
            .orElseThrow {
                VocabularyNotExistException(id)
            }
    }

}