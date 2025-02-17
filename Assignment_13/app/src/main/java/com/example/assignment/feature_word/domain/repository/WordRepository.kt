package com.example.assignment.feature_word.domain.repository

import android.net.Uri
import com.example.assignment.feature_word.domain.model.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {

    fun getWords(): Flow<List<Word>>

    suspend fun getWordById(id: Int): Word?

    suspend fun insertWord(word: Word)

    suspend fun deleteWord(word: Word)
}