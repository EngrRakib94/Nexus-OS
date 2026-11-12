package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskDialog(
    projects: List<ProjectItem>,
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        description: String,
        projectId: String,
        priority: TaskPriority,
        deadline: String,
        assignee: String,
        responsible: String,
        accountable: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedProjectId by remember { mutableStateOf(projects.firstOrNull()?.id ?: "") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.HIGH) }
    var deadline by remember { mutableStateOf("2026-11-25") }
    var assignee by remember { mutableStateOf("Commander Rakib") }
    var responsible by remember { mutableStateOf("Commander Rakib") }
    var accountable by remember { mutableStateOf("Lead Architect") }

    Dialog(onDismissRequest = onDismiss) {
        CyberCard(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            borderColor = NeonGreen,
            backgroundColor = CyberBackgroundSecondary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NEW OPERATION // TASK",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title", fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("task_title_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description / Specs", fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Priority Selection
                Text("PRIORITY LEVEL", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TaskPriority.values().forEach { prio ->
                        val isSelected = selectedPriority == prio
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSelected) NeonGreen.copy(alpha = 0.2f) else CyberSurface)
                                .border(1.dp, if (isSelected) NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                                .clickable { selectedPriority = prio }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = prio.label.take(4).uppercase(),
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NeonGreen else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Deadline
                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Deadline (YYYY-MM-DD)", fontFamily = FontFamily.Monospace, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // RACI inputs
                Text("RACI ACCOUNTABILITY", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = responsible,
                        onValueChange = { responsible = it },
                        label = { Text("Responsible (R)", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = accountable,
                        onValueChange = { accountable = it },
                        label = { Text("Accountable (A)", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                CyberButton(
                    text = "DEPLOY TASK TO DATABASE",
                    onClick = {
                        if (title.isNotBlank()) {
                            onConfirm(
                                title,
                                description,
                                selectedProjectId,
                                selectedPriority,
                                deadline,
                                assignee,
                                responsible,
                                accountable
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "submit_task_button"
                )
            }
        }
    }
}

@Composable
fun CreateTransactionDialog(
    currency: String,
    onDismiss: () -> Unit,
    onConfirm: (
        accountType: AccountType,
        type: TransactionType,
        amount: Double,
        category: String,
        description: String,
        date: String
    ) -> Unit
) {
    var accountType by remember { mutableStateOf(AccountType.PERSONAL) }
    var txType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var amountText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Tech Gear") }
    var description by remember { mutableStateOf("") }
    val currentDate = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    Dialog(onDismissRequest = onDismiss) {
        CyberCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = if (txType == TransactionType.INCOME) NeonGreen else NeonRed,
            backgroundColor = CyberBackgroundSecondary
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "NEW FINANCIAL ENTRY",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Account Type & Type Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AccountType.values().forEach { acc ->
                        val selected = accountType == acc
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (selected) NeonCyan.copy(alpha = 0.2f) else CyberSurface)
                                .border(1.dp, if (selected) NeonCyan else CyberBorder, RoundedCornerShape(4.dp))
                                .clickable { accountType = acc }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = acc.label.uppercase(),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = if (selected) NeonCyan else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TransactionType.values().forEach { t ->
                        val selected = txType == t
                        val col = if (t == TransactionType.INCOME) NeonGreen else NeonRed
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (selected) col.copy(alpha = 0.2f) else CyberSurface)
                                .border(1.dp, if (selected) col else CyberBorder, RoundedCornerShape(4.dp))
                                .clickable { txType = t }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = t.label.uppercase(),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = if (selected) col else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount ($currency)", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (e.g. Cloud, Food, Salary)", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                CyberButton(
                    text = "RECORD IN LEDGER",
                    onClick = {
                        val amt = amountText.toDoubleOrNull() ?: 0.0
                        if (amt > 0) {
                            onConfirm(accountType, txType, amt, category, description, currentDate)
                            onDismiss()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun TaskDetailsDialog(
    task: TaskItem,
    onDismiss: () -> Unit,
    onStatusChange: (TaskStatus) -> Unit,
    onDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        CyberCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = NeonCyan,
            backgroundColor = CyberBackgroundSecondary
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TASK TELEMETRY // ${task.id}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            color = NeonCyan
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusBadge(status = task.status)
                    PriorityBadge(priority = task.priority)
                }

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(color = CyberBorder)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "RACI RESPONSIBILITY MATRIX",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted,
                        fontSize = 9.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    RaciRoleBadge(role = "R", personName = task.responsible)
                    RaciRoleBadge(role = "A", personName = task.accountable)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "UPDATE STATUS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted,
                        fontSize = 9.sp
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TaskStatus.values().forEach { st ->
                        val isCurrent = task.status == st
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isCurrent) NeonGreen.copy(alpha = 0.2f) else CyberSurface)
                                .border(1.dp, if (isCurrent) NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                                .clickable { onStatusChange(st) }
                                .padding(vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = st.label.take(4).uppercase(),
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace,
                                color = if (isCurrent) NeonGreen else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            onDelete()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonRed.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.border(1.dp, NeonRed, RoundedCornerShape(4.dp))
                    ) {
                        Text("PURGE", color = NeonRed, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                    }

                    CyberButton(
                        text = "DONE",
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}
