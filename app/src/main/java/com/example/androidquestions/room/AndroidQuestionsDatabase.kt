package com.example.androidquestions.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.room.questions.QuestionsDao
import com.example.androidquestions.room.topics.Topic
import com.example.androidquestions.room.topics.TopicsDao

@Database(
    entities = [Topic::class, Question::class],
    version = 1,
    exportSchema = false
)
abstract class AndroidQuestionsDatabase : RoomDatabase() {
    abstract fun topicsDao(): TopicsDao
    abstract fun questionsDao(): QuestionsDao
}