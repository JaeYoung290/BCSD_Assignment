package com.example.assignment.feature_word.presentation.add_edit_word

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.feature_word.domain.model.InvalidWordException
import com.example.assignment.feature_word.domain.model.Word
import com.example.assignment.feature_word.domain.use_case.WordUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditWordViewModel @Inject constructor(
    private val wordUseCases: WordUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _wordName = MutableLiveData("")
    val wordName: LiveData<String> = _wordName

    private val _wordMeaning = MutableLiveData("")
    val wordMeaning: LiveData<String> = _wordMeaning

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _imageUri = MutableLiveData<String?>(null)
    val imageUri: LiveData<String?> = _imageUri

    private var currentWordId: Int? = null

    init {
        savedStateHandle.get<Int>("wordId")?.let { wordId ->
            if (wordId != -1) {
                viewModelScope.launch {
                    wordUseCases.getWord(wordId)?.also { word ->
                        currentWordId = word.id
                        _wordName.value = word.word
                        _wordMeaning.value = word.meaning
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditWordEvent) {
        when (event) {
            is AddEditWordEvent.EnteredWordName -> {
                _wordName.value = event.value
            }

            is AddEditWordEvent.EnteredMeaning -> {
                _wordMeaning.value = event.value
            }

            is AddEditWordEvent.SaveWord -> {
                viewModelScope.launch {
                    try {
                        wordUseCases.addWord(
                            Word(
                                word = wordName.value ?: "",
                                meaning = wordMeaning.value ?: "",
                                imageUri = event.imageUri,
                                id = currentWordId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveWord)
                    } catch (e: InvalidWordException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "단어 저장이 실패했습니다."
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveWord : UiEvent()
    }
}