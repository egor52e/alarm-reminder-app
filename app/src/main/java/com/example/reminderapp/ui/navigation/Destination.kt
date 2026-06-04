package com.example.reminderapp.ui.navigation

import com.example.reminderapp.R

enum class Destination(
    val route: String,
    val label: String,
    val iconID: Int,
    val contentDescription: String
) {
    REMINDERS("reminders", "Reminders", R.drawable.alarm, "Reminders"),
    TASK_FORM("task_form", "New", R.drawable.add, "New"),
    SETTINGS("settings", "Settings", R.drawable.settings, "Settings"),
}