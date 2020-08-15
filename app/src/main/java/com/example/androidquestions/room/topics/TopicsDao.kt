package com.example.androidquestions.room.topics

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TopicsDao {
    @Query("SELECT * FROM topics")
    fun getAll(): LiveData<List<Topic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(topics: List<Topic>)

    @Query("DELETE FROM topics")
    suspend fun clear()
}