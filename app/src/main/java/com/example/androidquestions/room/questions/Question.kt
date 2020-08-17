package com.example.androidquestions.room.questions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey
    val id: Int,
    val topicId: Int,
    val title: String,
    val url: String,
    val isInBookmarks: Boolean = false
)