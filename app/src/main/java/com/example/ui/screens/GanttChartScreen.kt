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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskItem
import com.example.data.model.TaskStatus
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun GanttChartScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            CyberCard(
                borderColor = NeonCyan,
                backgroundColor = CyberBackgroundSecondary
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GANTT TIMELINE MATRIX",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                        )
                        Text(
                            text = "CHRONO SCHEDULE // DEPENDENCY CONNECTIONS // RACI AVATARS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = TextMuted
                            )
                        )
                    }
                    CyberBadge(text = "${tasks.size} OPERATIONS", color = NeonCyan)
                }
            }
        }

        // Picture-accurate Gantt Chart Display
        item {
            PictureAccurateGanttChart(
                tasks = tasks,
                onTaskClick = { viewModel.selectedTaskForDetails.value = it }
            )
        }

        item {
            CyberSectionHeader(
                title = "Operation Detail Telemetry",
                subtitle = "Milestones, progress & responsible operatives"
            )
        }

        items(tasks, key = { it.id }) { task ->
            CyberCard(
                borderColor = if (task.status == TaskStatus.COMPLETED) CyberBorderSubtle else CyberBorder,
                backgroundColor = CyberSurface,
                cornerPadding = 10.dp,
                onClick = { viewModel.selectedTaskForDetails.value = task }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                        Text(
                            text = "Duration: ${task.durationDays} days // Span: ${task.startDate} -> ${task.deadline}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = TextMuted
                        )
                    }

                    StatusBadge(status = task.status)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        RaciRoleBadge(role = "R", personName = task.responsible)
                        RaciRoleBadge(role = "A", personName = task.accountable)
                    }

                    Text(
                        text = "PROJECT: ${task.projectName.take(16)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = NeonCyan
                        )
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
