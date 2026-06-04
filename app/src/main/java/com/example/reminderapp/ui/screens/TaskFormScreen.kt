package com.example.reminderapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reminderapp.data.Category
import com.example.reminderapp.data.Priority
import com.example.reminderapp.data.Task
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    taskToEdit: Task?,
    onNavigateBack: () -> Unit,
    onSaveTask: (Task) -> Unit
) {
    var title by remember(taskToEdit) { mutableStateOf(taskToEdit?.title ?: "") }
    var description by remember(taskToEdit) { mutableStateOf(taskToEdit?.description ?: "") }
    var selectedTime by remember(taskToEdit) { mutableStateOf(taskToEdit?.time ?: LocalTime.now()) }
    var selectedCategory by remember(taskToEdit) {
        mutableStateOf(
            Category.valueOf(taskToEdit?.category ?: Category.OTHER.name)
        )
    }
    var selectedPriority by remember(taskToEdit) {
        mutableStateOf(
            Priority.valueOf(taskToEdit?.priority ?: Priority.MEDIUM.name)
        )
    }
    var repeatDays by remember(taskToEdit) { mutableStateOf(taskToEdit?.repeatDays ?: 0) }
    var selectedSound by remember(taskToEdit) { mutableStateOf(taskToEdit?.sound ?: "Gentle Chime") }
    var showTimePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (taskToEdit == null) "New Task" else "Edit Task") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Time Picker Trigger
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .clickable { showTimePicker = true },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 56.sp)
                    )
                }
            }

            // Task Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Category
            Text("Category", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Category.values()) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        modifier = Modifier.clickable { selectedCategory = category },
                        shape = CircleShape,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
                    ) {
                        Text(
                            text = category.name,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Priority
            Text("Priority", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            SerenePrioritySelector(
                currentPriority = selectedPriority,
                onPrioritySelected = { selectedPriority = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Repeat
            Text("Repeat", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            SereneRepeatSelector(
                daysMask = repeatDays,
                onDaysChanged = { repeatDays = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Sound
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Reminder Sound", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Text(selectedSound, style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = { /* show sound picker */ }) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        val task = Task(
                            uid = taskToEdit?.uid ?: 0,
                            title = title,
                            description = description,
                            time = selectedTime,
                            category = selectedCategory.name,
                            priority = selectedPriority.name,
                            repeatDays = repeatDays,
                            sound = selectedSound,
                            isEnabled = taskToEdit?.isEnabled ?: true,
                            isComplete = taskToEdit?.isComplete ?: false
                        )
                        onSaveTask(task)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Save Task")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("OK") }
            },
            text = {
                SereneTimePicker(
                    initialHour = selectedTime.hour,
                    initialMinute = selectedTime.minute,
                    onTimeSelected = { hour, minute ->
                        selectedTime = LocalTime.of(hour, minute)
                    }
                )
            }
        )
    }
}

// Уникальные имена компонентов, чтобы не конфликтовать с Material
@Composable
fun SereneTimePicker(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    var selectedHour by remember { mutableIntStateOf(initialHour) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }
    var isAm by remember { mutableStateOf(initialHour < 12) }

    val hours = (1..12).toList()
    val minutes = (0..59).toList()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hours
            Column(
                modifier = Modifier
                    .height(140.dp)
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState(
                        initialFirstVisibleItemIndex = hours.indexOf(
                            if (selectedHour % 12 == 0) 12 else selectedHour % 12
                        )
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(hours) { hour ->
                        val isSelected = (hour == (if (selectedHour % 12 == 0) 12 else selectedHour % 12))
                        Text(
                            text = hour.toString().padStart(2, '0'),
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .clickable {
                                    val newHour = if (isAm) hour else hour + 12
                                    selectedHour = newHour
                                    onTimeSelected(selectedHour, selectedMinute)
                                }
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Text(":", style = MaterialTheme.typography.displayLarge)

            // Minutes
            Column(
                modifier = Modifier
                    .height(140.dp)
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState(initialFirstVisibleItemIndex = minutes.indexOf(selectedMinute)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(minutes) { minute ->
                        Text(
                            text = minute.toString().padStart(2, '0'),
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp),
                            color = if (minute == selectedMinute) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .clickable {
                                    selectedMinute = minute
                                    onTimeSelected(selectedHour, selectedMinute)
                                }
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // AM/PM
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        isAm = true
                        selectedHour = if (selectedHour >= 12) selectedHour - 12 else selectedHour
                        onTimeSelected(selectedHour, selectedMinute)
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAm) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (isAm) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("AM")
                }
                Button(
                    onClick = {
                        isAm = false
                        selectedHour = if (selectedHour < 12) selectedHour + 12 else selectedHour
                        onTimeSelected(selectedHour, selectedMinute)
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isAm) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (!isAm) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("PM")
                }
            }
        }
    }
}

@Composable
fun SerenePrioritySelector(
    currentPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        Priority.values().forEach { priority ->
            val isSelected = currentPriority == priority
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onPrioritySelected(priority) },
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) Color.White else Color.Transparent,
                shadowElevation = if (isSelected) 2.dp else 0.dp
            ) {
                Text(
                    text = priority.name,
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SereneRepeatSelector(
    daysMask: Int,
    onDaysChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = listOf("S", "M", "T", "W", "T", "F", "S")
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        days.forEachIndexed { index, label ->
            val isSelected = (daysMask shr index) and 1 == 1
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        val newMask = if (isSelected) daysMask and (1 shl index).inv()
                        else daysMask or (1 shl index)
                        onDaysChanged(newMask)
                    },
                shape = CircleShape,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}