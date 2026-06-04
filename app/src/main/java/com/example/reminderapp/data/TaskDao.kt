package com.example.reminderapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE uid IN (:taskIds)")
    suspend fun loadAllByIds(taskIds: IntArray): List<Task>

    @Query("SELECT * FROM task WHERE title LIKE :title")
    suspend fun findByTitle(title: String): Task

    @Insert
    suspend fun insert(tasks: Task)

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)
}