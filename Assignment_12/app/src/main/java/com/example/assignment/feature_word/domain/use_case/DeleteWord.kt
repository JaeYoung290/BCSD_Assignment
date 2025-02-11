package com.example.assignment.feature_word.domain.use_case

import com.example.assignment.feature_word.domain.model.Word
import com.example.assignment.feature_word.domain.repository.WordRepository

class DeleteWord(
    private val repository: WordRepository
) {
    suspend operator fun invoke(word: Word) {
        repository.deleteWord(word)
    }
}