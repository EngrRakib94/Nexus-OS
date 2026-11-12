package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun TaskManagerScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val googleSheetsSynced by viewModel.googleSheetsConnected.collectAsState()

    var selectedStatusFilter by remember { mutableStateOf<TaskStatus?>(null) }
    var selectedPriorityFilter by remember { mutableStateOf<TaskPriority?>(null) }
    var filterQuery by remember { mutableStateOf("") }
    var viewMode by remember { mutableStateOf("KANBAN") } // KANBAN or LIST

    val filteredTasks = tasks.filter { task ->
        (selectedStatusFilter == null || task.status == selectedStatusFilter) &&
        (selectedPriorityFilter == null || task.priority == selectedPriorityFilter) &&
        (filterQuery.isBlank() || task.title.contains(filterQuery, ignoreCase = true) || task.description.contains(filterQuery, ignoreCase = true))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Top bar with sync indicator & view switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CyberBadge(
                    text = if (googleSheetsSynced) "G-SHEETS // SYNCED" else "G-SHEETS // PAUSED",
                    color = if (googleSheetsSynced) NeonGreen else TextMuted
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Button(
                    onClick = { viewMode = "KANBAN" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewMode == "KANBAN") NeonGreen.copy(alpha = 0.2f) else CyberSurface
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.border(1.dp, if (viewMode == "KANBAN") NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                ) {
                    Text("KANBAN", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = if (viewMode == "KANBAN") NeonGreen else TextSecondary)
                }

                Button(
                    onClick = { viewMode = "LIST" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewMode == "LIST") NeonGreen.copy(alpha = 0.2f) else CyberSurface
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.border(1.dp, if (viewMode == "LIST") NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                ) {
                    Text("LIST", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = if (viewMode == "LIST") NeonGreen else TextSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search and Add Task Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = filterQuery,
                onValueChange = { filterQuery = it },
                placeholder = { Text("Search operations...", fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = CyberBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            CyberButton(
                text = "+ TASK",
                onClick = { viewModel.showCreateTaskDialog.value = true },
                testTag = "create_task_fab"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status Tabs for Kanban
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                val isAll = selectedStatusFilter == null
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isAll) NeonGreen.copy(alpha = 0.2f) else CyberSurface)
                        .border(1.dp, if (isAll) NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                        .clickable { selectedStatusFilter = null }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "ALL (${tasks.size})",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAll) NeonGreen else TextSecondary
                    )
                }
            }

            items(TaskStatus.values()) { status ->
                val isSelected = selectedStatusFilter == status
                val count = tasks.count { it.status == status }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else CyberSurface)
                        .border(1.dp, if (isSelected) NeonCyan else CyberBorder, RoundedCornerShape(4.dp))
                        .clickable { selectedStatusFilter = status }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${status.label.uppercase()} ($count)",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) NeonCyan else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tasks List
        if (filteredTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FilterListOff,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "NO OPERATIONS DETECTED IN THIS SECTOR",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onClick = { viewModel.selectedTaskForDetails.value = task },
                        onPromoteStatus = {
                            val nextStatus = when (task.status) {
                                TaskStatus.TODO -> TaskStatus.IN_PROGRESS
                                TaskStatus.IN_PROGRESS -> TaskStatus.COMPLETED
                                TaskStatus.PENDING -> TaskStatus.IN_PROGRESS
                                TaskStatus.COMPLETED -> TaskStatus.TODO
                                TaskStatus.CANCELLED -> TaskStatus.TODO
                            }
                            viewModel.updateTaskStatus(task.id, nextStatus)
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: TaskItem,
    onClick: () -> Unit,
    onPromoteStatus: () -> Unit
) {
    CyberCard(
        borderColor = when (task.priority) {
            TaskPriority.CRITICAL -> NeonRed.copy(alpha = 0.7f)
            TaskPriority.HIGH -> NeonAmber.copy(alpha = 0.6f)
            else -> CyberBorder
        },
        backgroundColor = CyberSurface,
        cornerPadding = 12.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
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
                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextSecondary,
                        maxLines = 2
                    )
                }
            }
            PriorityBadge(priority = task.priority)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Due: ${task.deadline}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                RaciRoleBadge(role = "R", personName = task.responsible)
                RaciRoleBadge(role = "A", personName = task.accountable)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = CyberBorderSubtle)
        Spacer(modifier = Modifier.height(6.dp))

        // Status row with quick promote button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusBadge(status = task.status)

            Button(
                onClick = onPromoteStatus,
                colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceElevated),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.border(1.dp, NeonGreen.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = when (task.status) {
                        TaskStatus.TODO -> "START ->"
                        TaskStatus.IN_PROGRESS -> "COMPLETE ->"
                        TaskStatus.PENDING -> "RESUME ->"
                        TaskStatus.COMPLETED -> "REOPEN"
                        TaskStatus.CANCELLED -> "RESTORE"
                    },
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonGreen
                )
            }
        }
    }
}
