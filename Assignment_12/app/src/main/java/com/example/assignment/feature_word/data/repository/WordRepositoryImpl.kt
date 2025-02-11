package com.example.assignment.feature_word.data.repository

import com.example.assignment.feature_word.data.data_source.WordDao
import com.example.assignment.feature_word.domain.model.Word
import com.example.assignment.feature_word.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

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