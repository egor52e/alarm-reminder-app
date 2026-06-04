package com.example.reminderapp.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TasksRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAll()

    @WorkerThread
    suspend fun insert(task: Task) = withContext(Dispatchers.IO) {
        taskDao.insert(task)
    }

    suspend fun delete(task: Task) = withContext(Dispatchers.IO) {
        taskDao.delete(task)
    }

    suspend fun update(task: Task) = withContext(Dispatchers.IO) {
        taskDao.update(task)
    }

}