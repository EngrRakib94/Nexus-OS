package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.NexusScreen
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasks.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val routines by viewModel.routines.collectAsState()
    val focusSessions by viewModel.focusSessions.collectAsState()
    val vocabulary by viewModel.vocabularyWords.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val waterIntakeMl by viewModel.waterIntakeMl.collectAsState()
    val dailyWaterGoalMl by viewModel.dailyWaterGoalMl.collectAsState()

    // Calculations
    val completedTasksCount = tasks.count { it.status == TaskStatus.COMPLETED }
    val pendingTasksCount = tasks.count { it.status == TaskStatus.IN_PROGRESS || it.status == TaskStatus.PENDING }
    val activeProjectsCount = projects.count { it.status == ProjectStatus.ACTIVE }

    val completedRoutines = routines.count { it.isCompletedToday }
    val routinePercent = if (routines.isNotEmpty()) (completedRoutines * 100) / routines.size else 0

    val todayFocusMinutes = focusSessions.sumOf { it.durationMinutes }

    val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val netBalance = totalIncome - totalExpense

    val activeWordIndex = remember { mutableStateOf(0) }
    val currentWord = if (vocabulary.isNotEmpty()) vocabulary[activeWordIndex.value % vocabulary.size] else null

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Cyber Welcome & Node Status Banner
        item {
            Spacer(modifier = Modifier.height(4.dp))
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
                            text = "COMMAND CENTER ONLINE",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = NeonGreen,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "CENTRAL DATA ENGINE ACTIVE // 0 DISCONNECTED NODES",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = TextSecondary,
                                fontSize = 9.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NeonGreen.copy(alpha = 0.15f))
                            .border(1.dp, NeonGreen, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "SYNCED",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // 2. VOCABULARY SECTION — MOVED TO TOP LINE (AS REQUESTED)
        item {
            CyberSectionHeader(
                title = "Vocabulary of the Day",
                subtitle = "English & Bengali linguistic mastery",
                actionText = "Rotate Word",
                onActionClick = { activeWordIndex.value = activeWordIndex.value + 1 }
            )

            if (currentWord != null) {
                CyberCard(
                    borderColor = NeonCyan,
                    backgroundColor = CyberSurface
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentWord.englishWord.uppercase(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan,
                                    letterSpacing = 1.sp
                                )
                            )
                            if (currentWord.pronunciation.isNotBlank()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = currentWord.pronunciation,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                        CyberBadge(
                            text = if (currentWord.isLearned) "LEARNED" else "NEW",
                            color = if (currentWord.isLearned) NeonGreen else NeonAmber
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "বাংলা অর্থ: ${currentWord.bengaliMeaning}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${currentWord.exampleSentence}\"",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    )
                }
            }
        }

        // 3. Quick Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.showCreateTaskDialog.value = true },
                    modifier = Modifier.weight(1f).testTag("quick_add_task_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreenContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+ TASK", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = NeonGreen)
                }

                Button(
                    onClick = { viewModel.navigateTo(NexusScreen.FOCUS_MODE) },
                    modifier = Modifier.weight(1f).testTag("quick_focus_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyanContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.HourglassTop, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("FOCUS", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = NeonCyan)
                }

                Button(
                    onClick = { viewModel.showCreateTransactionDialog.value = true },
                    modifier = Modifier.weight(1f).testTag("quick_expense_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurpleContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Paid, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+ LEDGER", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = NeonPurple)
                }
            }
        }

        // 4. 4 CORE TELEMETRY STAT WIDGETS — WITH DIRECT CLICKABLE NAVIGATION LINKS (AS REQUESTED)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // LINK 1: Tasks In Flight -> Task Manager
                    CyberStatWidget(
                        title = "Tasks In Flight",
                        value = "${tasks.size}",
                        subtitle = "$completedTasksCount done // $pendingTasksCount active (Tap to Open ->)",
                        modifier = Modifier.weight(1f),
                        accentColor = NeonGreen,
                        icon = Icons.Default.TaskAlt,
                        onClick = { viewModel.navigateTo(NexusScreen.TASK_MANAGER) }
                    )

                    // LINK 2: Routine Flow -> Routine Planner
                    CyberStatWidget(
                        title = "Routine Flow",
                        value = "$routinePercent%",
                        subtitle = "$completedRoutines of ${routines.size} complete (Tap to Open ->)",
                        modifier = Modifier.weight(1f),
                        accentColor = NeonCyan,
                        icon = Icons.Default.Loop,
                        onClick = { viewModel.navigateTo(NexusScreen.ROUTINE_PLANNER) }
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // LINK 3: Focus Telemetry -> Focus Mode
                    CyberStatWidget(
                        title = "Focus Telemetry",
                        value = "${todayFocusMinutes}m",
                        subtitle = "${focusSessions.size} deep sessions (Tap to Open ->)",
                        modifier = Modifier.weight(1f),
                        accentColor = NeonPurple,
                        icon = Icons.Default.HourglassBottom,
                        onClick = { viewModel.navigateTo(NexusScreen.FOCUS_MODE) }
                    )

                    // LINK 4: Net Liquidity -> Wallet
                    CyberStatWidget(
                        title = "Net Liquidity",
                        value = "${netBalance.toInt()}",
                        subtitle = "$currency reserve (Tap to Open ->)",
                        modifier = Modifier.weight(1f),
                        accentColor = NeonAmber,
                        icon = Icons.Default.AccountBalanceWallet,
                        onClick = { viewModel.navigateTo(NexusScreen.WALLET) }
                    )
                }
            }
        }

        // 5. HYDRATION & DRINKING WATER TELEMETRY WIDGET (NEW HEALTH FEATURE WITH ANIMATION)
        item {
            CyberHydrationWidget(
                currentMl = waterIntakeMl,
                targetMl = dailyWaterGoalMl,
                onDrinkWater = { amount -> viewModel.logWaterIntake(amount) },
                onReset = { viewModel.resetWaterIntake() }
            )
        }

        // 6. PICTURE-ACCURATE GANTT TIMELINE SCHEDULE
        item {
            PictureAccurateGanttChart(
                tasks = tasks,
                onTaskClick = { viewModel.selectedTaskForDetails.value = it }
            )
        }

        // 7. INTERACTIVE KANBAN BOARD OPERATION PIPELINE
        item {
            DashboardKanbanBoard(
                tasks = tasks,
                onTaskClick = { viewModel.selectedTaskForDetails.value = it },
                onPromoteStatus = { task, newStatus ->
                    viewModel.updateTaskStatus(task.id, newStatus)
                },
                onAddTask = { viewModel.showCreateTaskDialog.value = true },
                onOpenFullKanban = { viewModel.navigateTo(NexusScreen.TASK_MANAGER) }
            )
        }

        // 8. KPI STATUS DISTRIBUTION BAR GRAPH
        item {
            CyberKpiBarChart(tasks = tasks)
        }

        // 9. RACI OPERATIVE WORKLOAD ALLOCATION GRAPH
        item {
            CyberRaciWorkloadGraph(tasks = tasks)
        }

        // 10. PROJECT INITIATIVES DELIVERY GAUGE
        item {
            CyberProjectProgressGraph(projects = projects)
        }

        // 11. FINANCIAL CASH FLOW GRAPH
        item {
            CyberCard(
                borderColor = NeonAmber.copy(alpha = 0.5f),
                backgroundColor = CyberSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "FINANCE // INFLOW VS OUTFLOW GRAPH",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = NeonAmber,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "MONTHLY CASH FLOW & BALANCE ($currency)",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                            color = TextMuted
                        )
                    }
                    CyberBadge(text = "$currency TREND", color = NeonAmber)
                }

                Spacer(modifier = Modifier.height(10.dp))

                val maxVal = maxOf(totalIncome, totalExpense, 1.0)
                val incomeFrac = (totalIncome / maxVal).toFloat().coerceIn(0.08f, 1f)
                val expenseFrac = (totalExpense / maxVal).toFloat().coerceIn(0.08f, 1f)

                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("TOTAL INCOME", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = NeonGreen)
                        Text("+${totalIncome.toInt()} $currency", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = NeonGreen)
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(CyberBorder)) {
                        Box(modifier = Modifier.fillMaxWidth(incomeFrac).fillMaxHeight().background(NeonGreen))
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("TOTAL EXPENSE", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = NeonRed)
                        Text("-${totalExpense.toInt()} $currency", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = NeonRed)
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(CyberBorder)) {
                        Box(modifier = Modifier.fillMaxWidth(expenseFrac).fillMaxHeight().background(NeonRed))
                    }
                }
            }
        }

        // 12. DAILY ROUTINES GRID & HABIT STREAKS
        item {
            CyberSectionHeader(
                title = "Daily Routine Grid",
                subtitle = "Sustained habit streaks",
                actionText = "Manage All",
                onActionClick = { viewModel.navigateTo(NexusScreen.ROUTINE_PLANNER) }
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                routines.take(3).forEach { routine ->
                    CyberCard(
                        borderColor = if (routine.isCompletedToday) NeonGreen.copy(alpha = 0.5f) else CyberBorder,
                        backgroundColor = CyberSurface,
                        cornerPadding = 8.dp,
                        onClick = { viewModel.toggleRoutine(routine.id) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Checkbox(
                                    checked = routine.isCompletedToday,
                                    onCheckedChange = { viewModel.toggleRoutine(routine.id) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = NeonGreen,
                                        uncheckedColor = CyberBorder,
                                        checkmarkColor = Color.Black
                                    ),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = routine.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        color = if (routine.isCompletedToday) TextSecondary else TextPrimary
                                    )
                                    Text(
                                        text = "${routine.time} // ${routine.category}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                                        color = TextMuted
                                    )
                                }
                            }
                            CyberBadge(
                                text = "STREAK ${routine.streak}D",
                                color = if (routine.streak > 5) NeonAmber else NeonCyan
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
