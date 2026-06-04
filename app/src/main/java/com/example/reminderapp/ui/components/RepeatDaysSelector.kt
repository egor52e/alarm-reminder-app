package com.example.reminderapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.reminderapp.ui.theme.Surface

@Composable
fun RepeatDaysSelector(
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