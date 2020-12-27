package com.example.androidquestions.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidquestions.room.questions.Question
import com.example.androidquestions.room.questions.QuestionsDao
import com.example.androidquestions.room.technologies.TechnologiesDao
import com.example.androidquestions.room.technologies.Technology
import com.example.androidquestions.room.topics.Topic
import com.example.androidquestions.room.topics.TopicsDao

@Database(
    entities = [
        Technology::class,
        Topic::class,
        Question::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AndroidQuestionsDatabase : RoomDatabase() {
    abstract val technologiesDao: TechnologiesDao
    abstract val topicsDao: TopicsDao
    abstract val questionsDao: QuestionsDao
}