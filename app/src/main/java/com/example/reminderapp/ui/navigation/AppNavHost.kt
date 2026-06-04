package com.example.reminderapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.reminderapp.ui.TaskViewModel
import com.example.reminderapp.ui.screens.AlarmScreen
import com.example.reminderapp.ui.screens.RemindersScreen
import com.example.reminderapp.ui.screens.SettingsScreen
import com.example.reminderapp.ui.screens.TaskFormScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    taskViewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(Destination.REMINDERS.route) {
            RemindersScreen(
                taskViewModel = taskViewModel,
                onTaskClick = { task ->
                    navController.navigate("${Destination.TASK_FORM.route}?taskId=${task.uid}")
                },
                onAddClick = {
                    navController.navigate(Destination.TASK_FORM.route)
                }
            )
        }

        composable(Destination.TASK_FORM.route) {
            val taskId = it.arguments?.getString("taskId")?.toIntOrNull()
            val allTasks by taskViewModel.allTasks.collectAsStateWithLifecycle(initialValue = emptyList())
            val task = if (taskId != null) allTasks.find { it.uid == taskId } else null

            TaskFormScreen(
                taskToEdit = task,
                onNavigateBack = { navController.popBackStack() },
                onSaveTask = { newTask ->
                    if (taskId == null) taskViewModel.insertTask(newTask)
                    else taskViewModel.updateTask(newTask)
                    navController.popBackStack()
                }
            )
        }

        composable(Destination.SETTINGS.route) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }

        composable("active_alarm/{taskId}") {
            val taskId = it.arguments?.getString("taskId")?.toIntOrNull()
            val allTasks by taskViewModel.allTasks.collectAsStateWithLifecycle(initialValue = emptyList())
            val task = if (taskId != null) allTasks.find { it.uid == taskId } else null

            if (task != null) {
                AlarmScreen(
                    task = task,
                    onStop = { navController.popBackStack() },
                    onSnooze = { navController.popBackStack() }
                )
            }
        }
    }
}