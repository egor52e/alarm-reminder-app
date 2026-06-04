package com.example.reminderapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ui/components/VerticalTimePicker.kt
@Composable
fun VerticalTimePicker(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedHour by remember { mutableIntStateOf(initialHour) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }
    var isAm by remember { mutableStateOf(initialHour < 12) }

    val hoursRange = (1..12).toList()
    val minutesRange = (0..59).toList()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Часы
            Column(
                modifier = Modifier
                    .height(140.dp)
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState(initialFirstVisibleItemIndex = hoursRange.indexOf(selectedHour % 12)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(hoursRange) { hour ->
                        Text(
                            text = hour.toString().padStart(2, '0'),
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp),
                            color = if (hour == (selectedHour % 12)) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .clickable {
                                    val newHour = if (isAm) hour else hour + 12
                                    selectedHour = newHour
                                    onTimeSelected(selectedHour, selectedMinute)
                                }
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }

            Text(":", style = MaterialTheme.typography.displayLarge)

            // Минуты
            Column(
                modifier = Modifier
                    .height(140.dp)
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState(initialFirstVisibleItemIndex = minutesRange.indexOf(selectedMinute)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(minutesRange) { minute ->
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
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }

            // AM/PM переключатель
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    modifier = Modifier.clickable {
                        isAm = true
                        selectedHour = if (selectedHour >= 12) selectedHour - 12 else selectedHour
                        onTimeSelected(selectedHour, selectedMinute)
                    },
                    shape = CircleShape,
                    color = if (isAm) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (isAm) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Text("AM", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                }
                Surface(
                    modifier = Modifier.clickable {
                        isAm = false
                        selectedHour = if (selectedHour < 12) selectedHour + 12 else selectedHour
                        onTimeSelected(selectedHour, selectedMinute)
                    },
                    shape = CircleShape,
                    color = if (!isAm) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (!isAm) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Text("PM", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                }
            }
        }
    }
}