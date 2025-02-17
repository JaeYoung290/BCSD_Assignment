package com.example.assignment.feature_word.data.repository

import android.content.Context
import android.net.Uri
import com.example.assignment.feature_word.data.data_source.WordDao
import com.example.assignment.feature_word.domain.model.Word
import com.example.assignment.feature_word.domain.repository.WordRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileOutputStream

class WordRepositoryImpl(
    private val dao: WordDao
) : WordRepository {
    override fun getWords(): Flow<List<Word>> {
        return dao.getWords()
    }

    override suspend fun getWordById(id: Int): Word? {
        return dao.getWordById(id)
    }

    override suspend fun insertWord(word: Word) {
        dao.insertWord(word)
    }

    override suspend fun deleteWord(word: Word) {
        dao.deleteWord(word)
    }
}