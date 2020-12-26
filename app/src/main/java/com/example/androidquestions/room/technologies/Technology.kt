package com.example.androidquestions.room.technologies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "technologies")
data class Technology(
    @PrimaryKey
    val id: Int,
    val title: String
)