package com.example.assignment.feature_word.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.assignment.feature_word.domain.model.Word

@Database(entities = [Word::class], version = 2, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {

    abstract val wordDao : WordDao

    companion object {
        const val DATABASE_NAME = "words_db"
    }
}

val migration_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE word ADD COLUMN imageUri TEXT"
        )
    }
    
}