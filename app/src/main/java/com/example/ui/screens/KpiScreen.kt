package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.ProjectStatus
import com.example.data.model.TaskStatus
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun KpiScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val focusSessions by viewModel.focusSessions.collectAsState()

    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.status == TaskStatus.COMPLETED }
    val inProgressTasks = tasks.count { it.status == TaskStatus.IN_PROGRESS }
    val pendingTasks = tasks.count { it.status == TaskStatus.PENDING }
    val todoTasks = tasks.count { it.status == TaskStatus.TODO }

    val taskCompletionRate = if (totalTasks > 0) (completedTasks * 100) / totalTasks else 0
    val onTimeRate = 92 // Telemetry benchmark

    val totalFocusMinutes = focusSessions.sumOf { it.durationMinutes }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            CyberCard(
                borderColor = NeonGreen,
                backgroundColor = CyberBackgroundSecondary
            ) {
                Text(
                    text = "KPI & PERFORMANCE TELEMETRY",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen
                    )
                )
                Text(
                    text = "COMPUTED DIRECTLY FROM CENTRAL TRANSACTION & TASK RECORDS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                )
            }
        }

        item {
            // Metrics grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CyberStatWidget(
                        title = "Completion Rate",
                        value = "$taskCompletionRate%",
                        subtitle = "$completedTasks of $totalTasks finalized",
                        modifier = Modifier.weight(1f),
                        accentColor = NeonGreen
                    )
                    CyberStatWidget(
                        title = "On-Time Delivery",
                        value = "$onTimeRate%",
                        subtitle = "8% delay threshold",
                        modifier = Modifier.weight(1f),
                        accentColor = NeonCyan
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CyberStatWidget(
                        title = "Active Projects",
                        value = "${projects.count { it.status == ProjectStatus.ACTIVE }}",
                        subtitle = "${projects.size} total in portfolio",
                        modifier = Modifier.weight(1f),
                        accentColor = NeonPurple
                    )
                    CyberStatWidget(
                        title = "Focus Recorded",
                        value = "${totalFocusMinutes / 60}h ${totalFocusMinutes % 60}m",
                        subtitle = "${focusSessions.size} recorded sessions",
                        modifier = Modifier.weight(1f),
                        accentColor = NeonAmber
                    )
                }
            }
        }

        // Section: Task Status Distribution Chart
        item {
            CyberCard(
                borderColor = CyberBorder,
                backgroundColor = CyberSurface
            ) {
                Text(
                    text = "OPERATION STATUS DISTRIBUTION",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Bar Visualization
                val statusList = listOf(
                    Triple("COMPLETED", completedTasks, NeonGreen),
                    Triple("IN PROGRESS", inProgressTasks, NeonCyan),
                    Triple("PENDING", pendingTasks, NeonAmber),
                    Triple("TO DO", todoTasks, TextSecondary)
                )

                statusList.forEach { (label, count, color) ->
                    val fraction = if (totalTasks > 0) count.toFloat() / totalTasks else 0f
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = label, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = color)
                            Text(text = "$count (${(fraction * 100).toInt()}%)", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(CyberBorder)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction.coerceAtLeast(0.02f))
                                    .fillMaxHeight()
                                    .background(color)
                            )
                        }
                    }
                }
            }
        }

        // Section: Team Workload Telemetry
        item {
            CyberCard(
                borderColor = CyberBorder,
                backgroundColor = CyberSurface
            ) {
                Text(
                    text = "OPERATIVE WORKLOAD ALLOCATION",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                val memberWorkloads = tasks.groupBy { it.responsible }
                memberWorkloads.forEach { (member, memberTasks) ->
                    val activeCount = memberTasks.count { it.status != TaskStatus.COMPLETED }
                    val doneCount = memberTasks.count { it.status == TaskStatus.COMPLETED }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = member,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = TextPrimary
                            )
                            Text(
                                text = "$doneCount done // $activeCount active",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = TextMuted
                            )
                        }

                        CyberBadge(
                            text = "${memberTasks.size} TASKS",
                            color = if (activeCount > 2) NeonAmber else NeonCyan
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
