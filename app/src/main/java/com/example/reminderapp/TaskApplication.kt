package com.example.reminderapp

import android.app.Application
import com.example.reminderapp.data.AppDatabase
import com.example.reminderapp.data.TasksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TaskApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TasksRepository(database.taskDao()) }
}