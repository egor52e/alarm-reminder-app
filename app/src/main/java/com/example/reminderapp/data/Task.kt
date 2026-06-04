package com.example.reminderapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

enum class Priority { LOW, MEDIUM, HIGH }
enum class Category { WORK, PERSONAL, HEALTH, OTHER }

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val title: String,
    val description: String = "",
    val time: LocalTime,
    val category: String = Category.OTHER.name,
    val priority: String = Priority.MEDIUM.name,
    val repeatDays: Int = 0,
    val sound: String = "Gentle Chime",
    val isEnabled: Boolean = true,
    val isComplete: Boolean = false
)