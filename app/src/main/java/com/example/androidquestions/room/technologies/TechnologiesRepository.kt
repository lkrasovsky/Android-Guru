package com.example.androidquestions.room.technologies

import androidx.lifecycle.LiveData

class TechnologiesRepository(private val technologiesDao: TechnologiesDao) {

    fun getAll(): LiveData<List<Technology>> {
        return technologiesDao.getAll()
    }

    suspend fun insert(technologies: List<Technology>) {
        technologiesDao.insert(technologies)
    }

    suspend fun clear() {
        technologiesDao.clear()
    }
}