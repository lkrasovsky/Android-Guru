package com.example.androidquestions.room.topics

import androidx.lifecycle.LiveData

class TopicsRepository(private val topicsDao: TopicsDao) {

    fun getAll(): LiveData<List<Topic>> {
        return topicsDao.getAll()
    }

    suspend fun insert(topics: List<Topic>) {
        return topicsDao.insert(topics)
    }
}