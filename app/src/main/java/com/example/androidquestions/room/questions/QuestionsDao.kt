package com.example.androidquestions.room.questions

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuestionsDao {

    @Query("SELECT * FROM questions WHERE topicId = :topicId")
    fun getAllFilteredByTopic(topicId: Int): LiveData<List<Question>>

    @Query("SELECT * FROM questions WHERE title LIKE '%'||:title||'%'")
    fun getAllFilteredByTitle(title: String): LiveData<List<Question>>

    @Query("SELECT * FROM questions WHERE isInBookmarks")
    fun getBookmarks(): LiveData<List<Question>>

    @Query("SELECT * FROM questions WHERE id = :id")
    fun getById(id: Int): Question

    @Query("SELECT * FROM questions WHERE id = :id")
    fun getByIdLiveData(id: Int): LiveData<Question>

    @Insert
    suspend fun insert(questions: List<Question>)

    @Update
    suspend fun update(questions: Question)
}