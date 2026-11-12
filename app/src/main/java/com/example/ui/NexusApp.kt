package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun NexusApp(
    viewModel: NexusViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()

    // Dialog states
    val showCreateTask by viewModel.showCreateTaskDialog.collectAsState()
    val showCreateTx by viewModel.showCreateTransactionDialog.collectAsState()
    val selectedTaskForDetails by viewModel.selectedTaskForDetails.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Back handling: pop previous screen or close drawer
    BackHandler(enabled = drawerState.isOpen || currentScreen != NexusScreen.DASHBOARD) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else {
            val popped = viewModel.navigateBack()
            if (!popped) {
                viewModel.navigateTo(NexusScreen.DASHBOARD)
            }
        }
    }

    if (!currentUser.isLoggedIn) {
        AuthScreen(viewModel = viewModel)
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CyberDrawerContent(
                    currentScreen = currentScreen,
                    userProfile = currentUser,
                    unreadNotifications = unreadNotifications,
                    onSelectScreen = { screen ->
                        viewModel.navigateTo(screen)
                    },
                    onCloseDrawer = {
                        coroutineScope.launch { drawerState.close() }
                    },
                    onLogout = {
                        coroutineScope.launch { drawerState.close() }
                        viewModel.logout()
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    CyberTopBar(
                        currentScreen = currentScreen,
                        userProfile = currentUser,
                        unreadNotifications = unreadNotifications,
                        onMenuClick = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        onSearchClick = {
                            viewModel.navigateTo(NexusScreen.GLOBAL_SEARCH)
                        },
                        onNotificationClick = {
                            viewModel.navigateTo(NexusScreen.NOTIFICATIONS)
                        },
                        onProfileClick = {
                            viewModel.navigateTo(NexusScreen.SETTINGS)
                        }
                    )
                },
                containerColor = CyberBackground
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Crossfade(
                        targetState = currentScreen,
                        label = "screen_crossfade"
                    ) { screen ->
                        when (screen) {
                            NexusScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                            NexusScreen.TASK_MANAGER -> TaskManagerScreen(viewModel = viewModel)
                            NexusScreen.TODO_LIST -> TodoListScreen(viewModel = viewModel)
                            NexusScreen.CALENDAR -> CalendarScreen(viewModel = viewModel)
                            NexusScreen.ROUTINE_PLANNER -> RoutineScreen(viewModel = viewModel)
                            NexusScreen.PROJECTS -> ProjectsRaciScreen(viewModel = viewModel)
                            NexusScreen.RACI_MATRIX -> ProjectsRaciScreen(viewModel = viewModel)
                            NexusScreen.KPI_METRICS -> KpiScreen(viewModel = viewModel)
                            NexusScreen.GANTT_CHART -> GanttChartScreen(viewModel = viewModel)
                            NexusScreen.FOCUS_MODE -> FocusModeScreen(viewModel = viewModel)
                            NexusScreen.VOCABULARY -> VocabularyScreen(viewModel = viewModel)
                            NexusScreen.WALLET -> WalletScreen(viewModel = viewModel)
                            NexusScreen.GLOBAL_SEARCH -> GlobalSearchScreen(viewModel = viewModel)
                            NexusScreen.NOTIFICATIONS -> NotificationScreen(viewModel = viewModel)
                            NexusScreen.ACTIVITY_LOG -> ActivityLogScreen(viewModel = viewModel)
                            NexusScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                        }
                    }

                    // Cyber Feedback Toast Bar
                    if (feedbackMessage != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyberSurfaceElevated)
                                .border(1.dp, NeonGreen, RoundedCornerShape(6.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = feedbackMessage!!,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    color = NeonGreen,
                                    fontSize = 10.sp
                                )
                            )
                        }
                        LaunchedEffect(feedbackMessage) {
                            kotlinx.coroutines.delay(2200L)
                            viewModel.clearFeedback()
                        }
                    }
                }
            }
        }

        // Dialogs
        if (showCreateTask) {
            CreateTaskDialog(
                projects = projects,
                onDismiss = { viewModel.showCreateTaskDialog.value = false },
                onConfirm = { title, desc, projId, prio, deadline, assignee, resp, acc ->
                    viewModel.createTask(title, desc, projId, prio, deadline, assignee, resp, acc)
                }
            )
        }

        if (showCreateTx) {
            CreateTransactionDialog(
                currency = currency,
                onDismiss = { viewModel.showCreateTransactionDialog.value = false },
                onConfirm = { accType, txType, amt, cat, desc, date ->
                    viewModel.addTransaction(accType, txType, amt, cat, desc, date)
                }
            )
        }

        if (selectedTaskForDetails != null) {
            TaskDetailsDialog(
                task = selectedTaskForDetails!!,
                onDismiss = { viewModel.selectedTaskForDetails.value = null },
                onStatusChange = { newStatus ->
                    viewModel.updateTaskStatus(selectedTaskForDetails!!.id, newStatus)
                },
                onDelete = {
                    viewModel.deleteTask(selectedTaskForDetails!!.id)
                }
            )
        }
    }
}
