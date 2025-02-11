package com.example.assignment.feature_word.presentation.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.feature_word.domain.use_case.WordUseCases
import com.example.assignment.feature_word.presentation.words.WordState
import com.example.assignment.feature_word.presentation.words.WordsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsViewModel @Inject constructor(
    private val wordUseCases: WordUseCases
) : ViewModel() {

    private val _state = MutableLiveData(WordState())
    val state: LiveData<WordState> = _state

    init {
        getWords()
    }

    private fun getWords() {
        viewModelScope.launch {
            wordUseCases.getWords().collect { words ->
                _state.value = state.value?.copy(
                    words = words
                )
            }
        }
    }

    fun onEvent(event: WordsEvent) {
        when (event) {
            is WordsEvent.DeleteWord -> {
                viewModelScope.launch {
                    wordUseCases.deleteWord(event.word)
                }
            }
        }
    }
}