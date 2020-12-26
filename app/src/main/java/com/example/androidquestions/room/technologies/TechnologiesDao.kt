package com.example.androidquestions.room.technologies

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TechnologiesDao {

    @Query("SELECT * FROM technologies")
    fun getAll(): LiveData<List<Technology>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(technologies: List<Technology>)

    @Query("DELETE FROM technologies")
    suspend fun clear()
}