package com.example.assignment.feature_word.presentation.words

import com.example.assignment.feature_word.domain.model.Word

sealed class WordsEvent {
    data class DeleteWord(val word: Word): WordsEvent()
}