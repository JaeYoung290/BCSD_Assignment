package com.example.assignment.feature_word.presentation.add_edit_word

sealed class AddEditWordEvent {
    data class EnteredWordName(val value: String) : AddEditWordEvent()
    data class EnteredMeaning(val value: String) : AddEditWordEvent()
    object SaveWord : AddEditWordEvent()
}