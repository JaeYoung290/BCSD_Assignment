package com.example.assignment.feature_word.domain.use_case

import com.example.assignment.feature_word.domain.model.Word
import com.example.assignment.feature_word.domain.repository.WordRepository

class GetWord(
    private val repository: WordRepository
) {

    suspend operator fun invoke(id: Int): Word? {
        return repository.getWordById(id)
    }

}