package com.example.reminderapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.data.Task
import com.example.reminderapp.data.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TasksRepository) : ViewModel() {
    val allTasks: StateFlow<List<Task>> = repository.allTasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    private var _taskToEdit = MutableStateFlow<Task?>(null)
    val taskToEdit: StateFlow<Task?> = _taskToEdit
    fun setTaskToEdit(task: Task?) {
        _taskToEdit.value = task
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun insertTask(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun toggleTaskEnabled(task: Task, enabled: Boolean) {
        updateTask(task.copy(isEnabled = enabled))
    }
}

class TaskViewModelFactory(private val repository: TasksRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}