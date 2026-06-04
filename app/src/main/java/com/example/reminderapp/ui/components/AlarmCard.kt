package com.example.reminderapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.reminderapp.data.Task
import com.example.reminderapp.ui.theme.ActiveAlarmCardBackground
import com.example.reminderapp.ui.theme.SurfaceContainerLowest
import java.time.format.DateTimeFormatter

@Composable
fun AlarmCard(
    task: Task,
    onToggleEnabled: (Boolean) -> Unit,
    onDayClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBackground = if (task.isEnabled) ActiveAlarmCardBackground else SurfaceContainerLowest
    val cardAlpha = if (task.isEnabled) 1f else 0.8f

    val formattedTime = remember(task.time) {
        task.time.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
            .shadow(
                elevation = if (task.isEnabled) 8.dp else 4.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color(0x0F4B5A9C),
                spotColor = Color(0x0F4B5A9C)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (task.isEnabled) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Switch(
                    checked = task.isEnabled,
                    onCheckedChange = onToggleEnabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }

            // Days of week
            val days = listOf("S", "M", "T", "W", "T", "F", "S")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                days.forEachIndexed { index, label ->
                    val isSelected = (task.repeatDays shr index) and 1 == 1
                    val isActive = task.isEnabled
                    val backgroundColor = when {
                        isActive && isSelected -> MaterialTheme.colorScheme.primary
                        else -> Color.Transparent
                    }
                    val textColor = when {
                        isActive && isSelected -> MaterialTheme.colorScheme.onPrimary
                        isActive && !isSelected -> MaterialTheme.colorScheme.outline
                        else -> MaterialTheme.colorScheme.outline
                    }
                    val borderColor = if (isActive && !isSelected) MaterialTheme.colorScheme.outlineVariant else Color.Transparent

                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .border(1.dp, borderColor, CircleShape)
                            .clickable(enabled = isActive) { onDayClick(index) },
                        shape = CircleShape,
                        color = backgroundColor
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(label, style = MaterialTheme.typography.labelSmall, color = textColor)
                        }
                    }
                }
            }
        }
    }
}