package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.RoutineFrequency
import com.example.data.model.RoutineItem
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun RoutineScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val routines by viewModel.routines.collectAsState()
    var selectedTab by remember { mutableStateOf(RoutineFrequency.DAILY) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredRoutines = routines.filter { it.frequency == selectedTab }
    val completedCount = routines.count { it.isCompletedToday }
    val totalCount = routines.size
    val completionPercentage = if (totalCount > 0) (completedCount * 100) / totalCount else 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Progress Overview
        CyberCard(
            borderColor = NeonGreen,
            backgroundColor = CyberBackgroundSecondary
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ROUTINE COMPLETION MATRIX",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen
                        )
                    )
                    Text(
                        text = "$completedCount of $totalCount completed today",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }

                Text(
                    text = "$completionPercentage%",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { completionPercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = NeonGreen,
                trackColor = CyberBorder
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Frequency Selector: Daily, Weekly, Monthly
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            RoutineFrequency.values().forEach { freq ->
                val isSel = selectedTab == freq
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSel) NeonGreen.copy(alpha = 0.2f) else CyberSurface)
                        .border(1.dp, if (isSel) NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                        .clickable { selectedTab = freq }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = freq.label.uppercase(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) NeonGreen else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${selectedTab.label.uppercase()} HABIT PROTOCOLS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )
            )

            CyberButton(
                text = "+ ROUTINE",
                onClick = { showAddDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredRoutines, key = { it.id }) { routine ->
                CyberCard(
                    borderColor = if (routine.isCompletedToday) NeonGreen.copy(alpha = 0.5f) else CyberBorder,
                    backgroundColor = CyberSurface
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = routine.isCompletedToday,
                            onCheckedChange = { viewModel.toggleRoutine(routine.id) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = NeonGreen,
                                uncheckedColor = CyberBorder,
                                checkmarkColor = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = routine.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (routine.isCompletedToday) TextSecondary else TextPrimary
                            )
                            if (routine.description.isNotBlank()) {
                                Text(
                                    text = routine.description,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }
                            Text(
                                text = "Time: ${routine.time} // ${routine.category}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp,
                                    color = NeonCyan
                                )
                            )
                        }
                        CyberBadge(
                            text = "${routine.streak}D STREAK",
                            color = if (routine.streak >= 7) NeonAmber else NeonCyan
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showAddDialog) {
        var title by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        var time by remember { mutableStateOf("07:00 AM") }
        var category by remember { mutableStateOf("Productivity") }

        androidx.compose.ui.window.Dialog(onDismissRequest = { showAddDialog = false }) {
            CyberCard(
                borderColor = NeonGreen,
                backgroundColor = CyberBackgroundSecondary
            ) {
                Text(
                    text = "CREATE RECURRING ROUTINE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Routine Title", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Scheduled Time", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                CyberButton(
                    text = "REGISTER ROUTINE",
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.createRoutine(title, desc, time, selectedTab, category)
                            showAddDialog = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
