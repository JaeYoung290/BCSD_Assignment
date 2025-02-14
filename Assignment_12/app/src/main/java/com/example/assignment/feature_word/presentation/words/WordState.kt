package com.example.assignment.feature_word.presentation.words

import com.example.assignment.feature_word.domain.model.Word

data class WordState(
    val words: List<Word> = emptyList()
)
