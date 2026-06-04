package com.example.reminderapp.ui.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.example.reminderapp.data.Task
import kotlinx.coroutines.delay
import java.time.format.DateTimeFormatter

@Composable
fun AlarmScreen(
    task: Task,
    onStop: () -> Unit,
    onSnooze: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val backgroundColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.background,
        targetValue = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bgColor"
    )

    var rippleScale1 by remember { mutableStateOf(0f) }
    var rippleScale2 by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            rippleScale1 = 1f
            delay(2000)
            rippleScale1 = 0f
            delay(1000)
        }
    }
    LaunchedEffect(Unit) {
        delay(1000)
        while (true) {
            rippleScale2 = 1f
            delay(2000)
            rippleScale2 = 0f
            delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Ripple 1
        Box(
            modifier = Modifier
                .matchParentSize()
                .scale(rippleScale1 * 2.5f)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = (1 - rippleScale1) * 0.4f),
                    shape = CircleShape
                )
        )
        // Ripple 2
        Box(
            modifier = Modifier
                .matchParentSize()
                .scale(rippleScale2 * 2.5f)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = (1 - rippleScale2) * 0.4f),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.SelfImprovement,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Time to start your day",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = task.time.format(DateTimeFormatter.ofPattern("hh:mm a")),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onStop,
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Stop, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("STOP")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onSnooze,
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Snooze, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("SNOOZE FOR 10 MINS")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}