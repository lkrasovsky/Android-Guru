package com.example.androidquestions.room.topics

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class Topic(
    @PrimaryKey
    val id: Int,
    val title: String,
    val technologyId: Int
)