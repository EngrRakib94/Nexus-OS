package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskItem
import com.example.data.model.TaskPriority
import com.example.data.model.TaskStatus
import com.example.ui.theme.*

@Composable
fun DashboardKanbanBoard(
    tasks: List<TaskItem>,
    onTaskClick: (TaskItem) -> Unit,
    onPromoteStatus: (TaskItem, TaskStatus) -> Unit,
    onAddTask: () -> Unit,
    onOpenFullKanban: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // 4 Primary Kanban Columns
    val kanbanColumns = listOf(
        Triple(TaskStatus.TODO, NeonCyan, "TO DO"),
        Triple(TaskStatus.IN_PROGRESS, NeonGreen, "IN PROGRESS"),
        Triple(TaskStatus.PENDING, NeonAmber, "PENDING"),
        Triple(TaskStatus.COMPLETED, Color(0xFF22C55E), "COMPLETED")
    )

    CyberCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = NeonGreen.copy(alpha = 0.5f),
        backgroundColor = CyberSurface
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(14.dp)
                        .background(NeonGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "KANBAN BOARD // OPERATION PIPELINE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen,
                            letterSpacing = 0.8.sp
                        )
                    )
                    Text(
                        text = "DRAG / PROMOTE TASKS ACROSS STAGES",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                        color = TextMuted
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TextButton(
                    onClick = onAddTask,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("+ TASK", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = NeonGreen)
                }

                TextButton(
                    onClick = onOpenFullKanban,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("FULL VIEW", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = NeonCyan)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontally Scrollable Kanban Columns
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            kanbanColumns.forEach { (status, columnColor, columnTitle) ->
                val columnTasks = tasks.filter { it.status == status }

                // Column Container (Width: 220dp)
                Column(
                    modifier = Modifier
                        .width(220.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberBackgroundSecondary)
                        .border(1.dp, columnColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    // Column Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(columnColor)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = columnTitle,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = columnColor
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(columnColor.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = columnTasks.size.toString(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = columnColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = CyberBorderSubtle)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Column Task Cards
                    if (columnTasks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyberSurface.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "NO TASKS",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = TextMuted
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            columnTasks.forEach { task ->
                                DashboardKanbanCard(
                                    task = task,
                                    columnColor = columnColor,
                                    onClick = { onTaskClick(task) },
                                    onPromote = {
                                        val nextStatus = when (task.status) {
                                            TaskStatus.TODO -> TaskStatus.IN_PROGRESS
                                            TaskStatus.IN_PROGRESS -> TaskStatus.COMPLETED
                                            TaskStatus.PENDING -> TaskStatus.IN_PROGRESS
                                            TaskStatus.COMPLETED -> TaskStatus.TODO
                                            TaskStatus.CANCELLED -> TaskStatus.TODO
                                        }
                                        onPromoteStatus(task, nextStatus)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "SWIPE HORIZONTALLY TO ACCESS ALL 4 KANBAN PIPELINES",
            fontFamily = FontFamily.Monospace,
            fontSize = 8.sp,
            color = TextMuted
        )
    }
}

@Composable
private fun DashboardKanbanCard(
    task: TaskItem,
    columnColor: Color,
    onClick: () -> Unit,
    onPromote: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CyberSurface)
            .border(
                1.dp,
                when (task.priority) {
                    TaskPriority.CRITICAL -> NeonRed.copy(alpha = 0.6f)
                    TaskPriority.HIGH -> NeonAmber.copy(alpha = 0.5f)
                    else -> CyberBorder
                },
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    ),
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                PriorityBadge(priority = task.priority)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Due: ${task.deadline}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.2f))
                            .border(1.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "R",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.responsible.take(8),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextSecondary
                    )
                }

                // Stage advancement button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyberSurfaceElevated)
                        .border(1.dp, columnColor.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                        .clickable { onPromote() }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = when (task.status) {
                            TaskStatus.TODO -> "START >"
                            TaskStatus.IN_PROGRESS -> "DONE >"
                            TaskStatus.PENDING -> "RESUME >"
                            TaskStatus.COMPLETED -> "REOPEN"
                            TaskStatus.CANCELLED -> "RESTORE"
                        },
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = columnColor
                    )
                }
            }
        }
    }
}
