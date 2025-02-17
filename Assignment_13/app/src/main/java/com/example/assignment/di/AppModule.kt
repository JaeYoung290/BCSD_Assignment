package com.example.assignment.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.assignment.feature_word.data.data_source.WordDatabase
import com.example.assignment.feature_word.data.data_source.migration_1_2
import com.example.assignment.feature_word.data.repository.WordRepositoryImpl
import com.example.assignment.feature_word.domain.repository.WordRepository
import com.example.assignment.feature_word.domain.use_case.AddWord
import com.example.assignment.feature_word.domain.use_case.DeleteWord
import com.example.assignment.feature_word.domain.use_case.GetWord
import com.example.assignment.feature_word.domain.use_case.GetWords
import com.example.assignment.feature_word.domain.use_case.WordUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWordDatabase(app: Application): WordDatabase {
        return Room.databaseBuilder(
            app,
            WordDatabase::class.java,
            WordDatabase.DATABASE_NAME
        ).addMigrations(migration_1_2).build()
    }

    @Provides
    @Singleton
    fun provideWordRepository(db: WordDatabase): WordRepository {
        return WordRepositoryImpl(db.wordDao)
    }

    @Provides
    @Singleton
    fun provideWordUseCases(repository: WordRepository): WordUseCases {
        return WordUseCases(
            getWords = GetWords(repository),
            deleteWord = DeleteWord(repository),
            addWord = AddWord(repository),
            getWord = GetWord(repository)
        )
    }
}