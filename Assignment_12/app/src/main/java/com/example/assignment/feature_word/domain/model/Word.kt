package com.example.assignment.feature_word.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(
    val word: String,
    val meaning: String,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)

class InvalidWordException(message: String): Exception(message)