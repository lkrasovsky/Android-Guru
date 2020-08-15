package com.example.androidquestions.koin

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.androidquestions.room.AndroidQuestionsDatabase
import com.example.androidquestions.room.questions.QuestionsRepository
import com.example.androidquestions.room.topics.TopicsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val module = module {
    // SharedPreferences
    single { getSharedPreferences(androidContext()) }

    // Room
    single {
        return@single Room.databaseBuilder(
            androidContext(), AndroidQuestionsDatabase::class.java, "android_questions_database"
        ).build()
    }
    single {
        val db = get() as AndroidQuestionsDatabase
        return@single db.topicsDao()
    }
    single {
        val db = get() as AndroidQuestionsDatabase
        return@single db.questionsDao()
    }
    single { TopicsRepository(get()) }
    single { QuestionsRepository(get()) }
}

fun getSharedPreferences(context: Context): SharedPreferences? {
    return context.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
}
