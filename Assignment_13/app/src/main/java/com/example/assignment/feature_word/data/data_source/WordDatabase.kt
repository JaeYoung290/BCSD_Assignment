package com.example.assignment.feature_word.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.assignment.feature_word.domain.model.Word

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {

    abstract val wordDao : WordDao

    companion object {
        const val DATABASE_NAME = "words_db"
    }
}