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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskItem
import com.example.data.model.TaskPriority
import com.example.data.model.TaskStatus
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun TodoListScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    var quickTaskTitle by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ACTIVE") } // ACTIVE, ALL, COMPLETED

    val activeTasks = tasks.filter { it.status != TaskStatus.COMPLETED && it.status != TaskStatus.CANCELLED }
    val completedTasks = tasks.filter { it.status == TaskStatus.COMPLETED }

    val displayedTasks = when (selectedFilter) {
        "ACTIVE" -> activeTasks
        "COMPLETED" -> completedTasks
        else -> tasks
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Quick Task Entry Field
        CyberCard(
            borderColor = NeonGreen,
            backgroundColor = CyberBackgroundSecondary
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = quickTaskTitle,
                    onValueChange = { quickTaskTitle = it },
                    placeholder = { Text("Quick capture mission / task...", fontSize = 12.sp, fontFamily = FontFamily.Monospace) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                CyberButton(
                    text = "ADD",
                    onClick = {
                        if (quickTaskTitle.isNotBlank()) {
                            viewModel.createTask(
                                title = quickTaskTitle,
                                description = "Quick captured to-do",
                                projectId = "proj-01",
                                priority = TaskPriority.HIGH,
                                deadline = "2026-11-20",
                                assignee = "Commander Rakib",
                                responsible = "Commander Rakib",
                                accountable = "Commander Rakib"
                            )
                            quickTaskTitle = ""
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter chips: Active, Completed, All
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ACTIVE" to "${activeTasks.size} PENDING", "COMPLETED" to "${completedTasks.size} DONE", "ALL" to "${tasks.size} TOTAL").forEach { (key, label) ->
                val isSel = selectedFilter == key
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSel) NeonGreen.copy(alpha = 0.2f) else CyberSurface)
                        .border(1.dp, if (isSel) NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                        .clickable { selectedFilter = key }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) NeonGreen else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // To-Do items
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(displayedTasks, key = { it.id }) { task ->
                val isDone = task.status == TaskStatus.COMPLETED
                CyberCard(
                    borderColor = if (isDone) CyberBorderSubtle else CyberBorder,
                    backgroundColor = if (isDone) CyberSurface.copy(alpha = 0.6f) else CyberSurface,
                    cornerPadding = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isDone,
                            onCheckedChange = { checked ->
                                viewModel.updateTaskStatus(
                                    task.id,
                                    if (checked) TaskStatus.COMPLETED else TaskStatus.TODO
                                )
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = NeonGreen,
                                uncheckedColor = CyberBorder,
                                checkmarkColor = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Medium,
                                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                                ),
                                color = if (isDone) TextMuted else TextPrimary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Due: ${task.deadline} // ${task.projectName}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                                    color = TextMuted
                                )
                            }
                        }
                        PriorityBadge(priority = task.priority)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
