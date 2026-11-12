package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun FocusModeScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val focusSessions by viewModel.focusSessions.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    var selectedDurationMinutes by remember { mutableStateOf(25) }
    var remainingSeconds by remember { mutableStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }
    var selectedTaskName by remember { mutableStateOf<String?>("Core Operations Deep Work") }

    // Timer effect
    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        } else if (isRunning && remainingSeconds == 0) {
            isRunning = false
            viewModel.finishFocusSession(selectedDurationMinutes, selectedTaskName)
            remainingSeconds = selectedDurationMinutes * 60
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)
    val totalSeconds = selectedDurationMinutes * 60
    val progress = if (totalSeconds > 0) (totalSeconds - remainingSeconds).toFloat() / totalSeconds else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Preset duration selectors
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(25, 45, 60).forEach { mins ->
                val isSel = selectedDurationMinutes == mins
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) NeonPurple.copy(alpha = 0.2f) else CyberSurface)
                        .border(1.dp, if (isSel) NeonPurple else CyberBorder, RoundedCornerShape(6.dp))
                        .clickable {
                            if (!isRunning) {
                                selectedDurationMinutes = mins
                                remainingSeconds = mins * 60
                            }
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$mins MINS",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) NeonPurple else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Circular futuristic HUD Timer
        Box(
            modifier = Modifier
                .size(230.dp)
                .clip(CircleShape)
                .background(CyberSurface)
                .border(2.dp, if (isRunning) NeonPurple else CyberBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize().padding(8.dp),
                color = NeonPurple,
                trackColor = CyberBorderSubtle,
                strokeWidth = 6.dp
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isRunning) "FOCUS ENGAGED" else "READY // STANDBY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = if (isRunning) NeonPurple else TextMuted,
                        letterSpacing = 1.sp,
                        fontSize = 9.sp
                    )
                )

                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 44.sp,
                        color = if (isRunning) NeonPurple else TextPrimary
                    )
                )

                if (selectedTaskName != null) {
                    Text(
                        text = selectedTaskName!!.take(20),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            color = NeonCyan
                        ),
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Control Buttons: Start/Pause, Reset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { isRunning = !isRunning },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) NeonAmber else NeonPurple
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.padding(horizontal = 6.dp)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isRunning) "PAUSE PROTOCOL" else "ENGAGE FOCUS",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Button(
                onClick = {
                    isRunning = false
                    remainingSeconds = selectedDurationMinutes * 60
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceElevated),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Focus Session History
        CyberSectionHeader(
            title = "Focus Chrono History",
            subtitle = "Recorded deep work intervals"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(focusSessions, key = { it.id }) { session ->
                CyberCard(
                    borderColor = CyberBorder,
                    backgroundColor = CyberSurface,
                    cornerPadding = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = session.taskName ?: "Deep System Architecture",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = TextPrimary
                            )
                            Text(
                                text = "${session.durationMinutes} Minutes Session",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = TextSecondary
                            )
                        }
                        CyberBadge(
                            text = "+${session.durationMinutes}M",
                            color = NeonPurple
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
