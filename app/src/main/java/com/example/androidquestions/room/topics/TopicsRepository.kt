package com.example.androidquestions.room.topics

import androidx.lifecycle.LiveData

class TopicsRepository(private val topicsDao: TopicsDao) {

    fun getAllFilteredByTechnology(technologyId: Int): LiveData<List<Topic>> {
        return topicsDao.getAllFilteredByTechnology(technologyId)
    }

    suspend fun insert(topics: List<Topic>) {
        topicsDao.insert(topics)
    }

    suspend fun clear() {
        topicsDao.clear()
    }
}