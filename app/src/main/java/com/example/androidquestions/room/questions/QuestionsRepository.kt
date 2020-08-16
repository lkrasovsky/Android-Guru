package com.example.androidquestions.room.questions

import androidx.lifecycle.LiveData

class QuestionsRepository(private val questionsDao: QuestionsDao) {
    fun getAllFilteredByTopic(topicId: Int): LiveData<List<Question>> {
        return questionsDao.getAllFilteredByTopic(topicId)
    }

    fun getById(id: Int): Question {
        return questionsDao.getById(id)
    }

    suspend fun insert(questions: List<Question>) {
        return questionsDao.insert(questions)
    }
}