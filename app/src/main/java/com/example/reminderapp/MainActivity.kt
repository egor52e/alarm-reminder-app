package com.example.reminderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reminderapp.ui.TaskViewModel
import com.example.reminderapp.ui.TaskViewModelFactory
import com.example.reminderapp.ui.components.NavBar
import com.example.reminderapp.ui.navigation.AppNavHost
import com.example.reminderapp.ui.navigation.Destination
import com.example.reminderapp.ui.theme.ReminderAppTheme

class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TaskApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ReminderAppTheme {
                val navController = rememberNavController()
                val startDestination = Destination.REMINDERS

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute == Destination.REMINDERS.route || currentRoute == Destination.SETTINGS.route) {
                            NavBar(
                                navController = navController,
                                currentRoute = currentRoute,
                                onItemClick = { destination ->
                                    if (destination == Destination.TASK_FORM) {
                                        taskViewModel.setTaskToEdit(null)
                                    }
                                    if (currentRoute != destination.route) {
                                        navController.navigate(destination.route) {
                                            popUpTo(startDestination.route) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { contentPadding ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        AppNavHost(
                            navController = navController,
                            startDestination = startDestination,
                            taskViewModel = taskViewModel,
                            modifier = Modifier.padding(contentPadding)
                        )
                    }
                }
            }
        }
    }
}