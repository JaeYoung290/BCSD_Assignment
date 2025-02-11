package com.example.assignment.feature_word.domain.use_case

data class WordUseCases(
    val getWords: GetWords,
    val deleteWord: DeleteWord,
    val addWord: AddWord,
    val getWord: GetWord
)
