package com.example.androidquestions.room.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class QuestionsRepository(private val questionsDao: QuestionsDao) {

    fun getAllFilteredByTopic(topicId: Int): LiveData<List<Question>> {
        return questionsDao.getAllFilteredByTopic(topicId)
    }

    fun getAllFilteredByTitle(title: String): LiveData<List<Question>> {
        return if (title.isNotBlank()) {
            questionsDao.getAllFilteredByTitle(title)
        } else {
            val liveData = MutableLiveData<List<Question>>()
            liveData.postValue(listOf())
            return liveData
        }
    }

    fun getBookmarks(): LiveData<List<Question>> {
        return questionsDao.getBookmarks()
    }

    fun getById(id: Int): Question {
        return questionsDao.getById(id)
    }

    fun getByIdLiveData(id: Int): LiveData<Question> {
        return questionsDao.getByIdLiveData(id)
    }

    suspend fun insert(questions: List<Question>) {
        return questionsDao.insert(questions)
    }

    suspend fun update(questions: Question) {
        return questionsDao.update(questions)
    }
}