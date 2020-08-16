package com.example.androidquestions.room.questions

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionsDao {
    @Query("SELECT * FROM questions WHERE topicId = :topicId")
    fun getAllFilteredByTopic(topicId: Int): LiveData<List<Question>>

    @Query("SELECT * FROM questions WHERE id = :id")
    fun getById(id: Int): Question

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questions: List<Question>)
}