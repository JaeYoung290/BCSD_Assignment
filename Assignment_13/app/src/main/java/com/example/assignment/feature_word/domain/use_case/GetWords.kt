package com.example.assignment.feature_word.domain.use_case

import com.example.assignment.feature_word.domain.model.Word
import com.example.assignment.feature_word.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

class GetWords(
    private val repository: WordRepository
) {

    operator fun invoke(): Flow<List<Word>> {
        return repository.getWords()
    }
}