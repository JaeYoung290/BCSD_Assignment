package com.example.assignment.feature_word.domain.use_case

import com.example.assignment.feature_word.domain.model.InvalidWordException
import com.example.assignment.feature_word.domain.model.Word
import com.example.assignment.feature_word.domain.repository.WordRepository

class AddWord(
    private val repository: WordRepository
) {

    @Throws(InvalidWordException::class)
    suspend operator fun invoke(word: Word) {
        if(word.word.isBlank()) {
            throw InvalidWordException("단어명 항목이 비어있습니다.")
        }
        if(word.meaning.isBlank()) {
            throw InvalidWordException("단어 뜻 항목이 비어있습니다.")
        }
        repository.insertWord(word)
    }
}