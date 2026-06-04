package com.example.reminderapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.reminderapp.data.Task
import com.example.reminderapp.ui.TaskViewModel
import com.example.reminderapp.ui.components.AlarmCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    taskViewModel: TaskViewModel,
    onTaskClick: (Task) -> Unit,
    onAddClick: () -> Unit
) {
    val tasks by taskViewModel.allTasks.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Serene Chronos") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { /* menu */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(tasks, key = { it.uid }) { task ->
                AlarmCard(
                    task = task,
                    onToggleEnabled = { enabled ->
                        taskViewModel.updateTask(task.copy(isEnabled = enabled))
                    },
                    onDayClick = { dayIndex ->
                        val newMask = if ((task.repeatDays shr dayIndex) and 1 == 1)
                            task.repeatDays and (1 shl dayIndex).inv()
                        else
                            task.repeatDays or (1 shl dayIndex)
                        taskViewModel.updateTask(task.copy(repeatDays = newMask))
                    }
                )
            }
        }
    }
}