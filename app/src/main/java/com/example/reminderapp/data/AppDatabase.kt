package com.example.reminderapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime

@Database(entities = [com.example.reminderapp.data.Task::class], version = 1, exportSchema = false)
@TypeConverters(TimeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tasks_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(TaskDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task ADD COLUMN category TEXT NOT NULL DEFAULT 'OTHER'")
                database.execSQL("ALTER TABLE task ADD COLUMN priority TEXT NOT NULL DEFAULT 'MEDIUM'")
                database.execSQL("ALTER TABLE task ADD COLUMN repeatDays INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE task ADD COLUMN sound TEXT NOT NULL DEFAULT 'Gentle Chime'")
                database.execSQL("ALTER TABLE task ADD COLUMN isEnabled INTEGER NOT NULL DEFAULT 1")
            }
        }
    }

    private class TaskDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    val taskDao = database.taskDao()

                    val task = Task(
                        uid = 1,
                        title = "Task",
                        description = "new task",
                        time = LocalTime.of(7, 40),
                        category = "work",
                        isComplete = false
                    )
                    taskDao.insert(task)
                    val task2 = Task(
                        uid = 2,
                        title = "Task 2",
                        description = "new task 2",
                        time = LocalTime.of(12, 20),
                        category = "Self",
                        isComplete = true
                    )
                    taskDao.insert(task2)
                }
            }
        }
    }
}