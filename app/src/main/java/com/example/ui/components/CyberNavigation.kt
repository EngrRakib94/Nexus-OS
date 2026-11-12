package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.NexusScreen
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyberTopBar(
    currentScreen: NexusScreen,
    userProfile: UserProfile,
    unreadNotifications: Int,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "NEXUS OS",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(NeonCyan.copy(alpha = 0.15f))
                            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(3.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "CMD-CTR",
                            color = NeonCyan,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = currentScreen.label.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.testTag("nav_menu_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Navigation Menu",
                    tint = NeonGreen
                )
            }
        },
        actions = {
            // Search button
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.testTag("global_search_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextSecondary
                )
            }

            // Notification button with badge
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.testTag("notifications_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotifications > 0) {
                            Badge(
                                containerColor = NeonRed,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = unreadNotifications.toString(),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = if (unreadNotifications > 0) NeonAmber else TextSecondary
                    )
                }
            }

            // Profile Avatar Chip
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(CircleShape)
                    .background(CyberSurfaceElevated)
                    .border(1.dp, NeonGreen.copy(alpha = 0.5f), CircleShape)
                    .clickable { onProfileClick() }
                    .padding(horizontal = 7.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userProfile.avatarInitials,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen,
                        fontSize = 10.sp
                    )
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CyberBackgroundSecondary,
            titleContentColor = TextPrimary
        )
    )
}

@Composable
fun CyberDrawerContent(
    currentScreen: NexusScreen,
    userProfile: UserProfile,
    unreadNotifications: Int,
    onSelectScreen: (NexusScreen) -> Unit,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()

    ModalDrawerSheet(
        drawerContainerColor = CyberBackgroundSecondary,
        drawerContentColor = TextPrimary,
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonGreen.copy(alpha = 0.15f))
                        .border(1.dp, NeonGreen, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = null,
                        tint = NeonGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "NEXUS OS",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "PERSONAL COMMAND CENTER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            color = TextSecondary,
                            fontSize = 8.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            // Live Telemetry bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(CyberSurface)
                    .border(1.dp, CyberBorder, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PulsingNodeIndicator()
                Text(
                    text = "ROLE: ${userProfile.role.label.uppercase()}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = NeonCyan,
                        fontSize = 9.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Sections
            val sections = listOf(
                "MAIN CONTROL" to listOf(
                    Triple(NexusScreen.DASHBOARD, Icons.Default.Dashboard, "Dashboard"),
                    Triple(NexusScreen.TASK_MANAGER, Icons.Default.ViewKanban, "Task Manager"),
                    Triple(NexusScreen.TODO_LIST, Icons.Default.Checklist, "To-Do List"),
                    Triple(NexusScreen.CALENDAR, Icons.Default.CalendarMonth, "Calendar"),
                    Triple(NexusScreen.ROUTINE_PLANNER, Icons.Default.Loop, "Routine Planner")
                ),
                "PROJECT MANAGEMENT" to listOf(
                    Triple(NexusScreen.PROJECTS, Icons.Default.AccountTree, "Projects"),
                    Triple(NexusScreen.RACI_MATRIX, Icons.Default.GroupWork, "RACI Matrix"),
                    Triple(NexusScreen.KPI_METRICS, Icons.Default.ShowChart, "KPI & Metrics"),
                    Triple(NexusScreen.GANTT_CHART, Icons.Default.Timeline, "Gantt Chart")
                ),
                "PERSONAL DEVELOPMENT" to listOf(
                    Triple(NexusScreen.FOCUS_MODE, Icons.Default.HourglassTop, "Focus Mode"),
                    Triple(NexusScreen.VOCABULARY, Icons.Default.Translate, "Vocabulary Learning")
                ),
                "FINANCIAL MANAGEMENT" to listOf(
                    Triple(NexusScreen.WALLET, Icons.Default.AccountBalanceWallet, "Wallet & Expenses")
                ),
                "SYSTEM" to listOf(
                    Triple(NexusScreen.GLOBAL_SEARCH, Icons.Default.Search, "Global Search"),
                    Triple(NexusScreen.NOTIFICATIONS, Icons.Default.Notifications, "Notifications"),
                    Triple(NexusScreen.ACTIVITY_LOG, Icons.Default.History, "Activity Log"),
                    Triple(NexusScreen.SETTINGS, Icons.Default.Settings, "Settings")
                )
            )

            sections.forEach { (sectionTitle, items) ->
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(start = 4.dp, top = 10.dp, bottom = 4.dp)
                )

                items.forEach { (screen, icon, label) ->
                    val isSelected = currentScreen == screen
                    val bg = if (isSelected) NeonGreen.copy(alpha = 0.12f) else Color.Transparent
                    val border = if (isSelected) NeonGreen.copy(alpha = 0.4f) else Color.Transparent
                    val textCol = if (isSelected) NeonGreen else TextPrimary
                    val iconTint = if (isSelected) NeonGreen else TextSecondary

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(bg)
                            .border(1.dp, border, RoundedCornerShape(6.dp))
                            .clickable {
                                onSelectScreen(screen)
                                onCloseDrawer()
                            }
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            ),
                            color = textCol,
                            modifier = Modifier.weight(1f)
                        )
                        if (screen == NexusScreen.NOTIFICATIONS && unreadNotifications > 0) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(NeonRed)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = unreadNotifications.toString(),
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = CyberBorder)
            Spacer(modifier = Modifier.height(10.dp))

            // Footer user info & Logout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(CyberSurface)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = userProfile.email,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            color = TextMuted
                        ),
                        maxLines = 1
                    )
                }
                IconButton(
                    onClick = onLogout,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = NeonRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
