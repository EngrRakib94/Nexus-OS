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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationType
import com.example.data.model.UserRole
import com.example.ui.NexusScreen
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

// 1. GLOBAL SEARCH SCREEN
@Composable
fun GlobalSearchScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Search tasks, projects, routines, vocab, ledger...", fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("global_search_input"),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonGreen) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = CyberBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (searchQuery.isBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ManageSearch, contentDescription = null, tint = TextMuted, modifier = Modifier.size(44.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "GLOBAL TELEMETRY SCAN READY",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Enter query to scan central database",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        } else {
            Text(
                text = "FOUND ${searchResults.size} ENTRIES",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    color = NeonGreen
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(searchResults, key = { it.type + "_" + it.id }) { item ->
                    CyberCard(
                        borderColor = CyberBorder,
                        backgroundColor = CyberSurface,
                        cornerPadding = 10.dp,
                        onClick = { viewModel.navigateTo(item.targetScreen) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = TextPrimary
                                )
                                Text(
                                    text = item.subtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    color = TextSecondary
                                )
                            }
                            CyberBadge(text = item.type, color = NeonCyan)
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

// 2. NOTIFICATIONS SCREEN
@Composable
fun NotificationScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SYSTEM NOTIFICATION LOG",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = NeonAmber
                )
            )

            TextButton(onClick = { viewModel.markAllNotificationsRead() }) {
                Text("MARK ALL READ", fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = NeonCyan)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(notifications, key = { it.id }) { ntf ->
                val badgeColor = when (ntf.type) {
                    NotificationType.DEADLINE -> NeonRed
                    NotificationType.OVERDUE -> NeonRed
                    NotificationType.ROUTINE -> NeonCyan
                    NotificationType.BUDGET -> NeonAmber
                    NotificationType.SYSTEM -> NeonGreen
                }

                CyberCard(
                    borderColor = if (!ntf.isRead) badgeColor.copy(alpha = 0.6f) else CyberBorder,
                    backgroundColor = if (!ntf.isRead) CyberSurfaceElevated else CyberSurface,
                    cornerPadding = 10.dp,
                    onClick = { viewModel.markNotificationRead(ntf.id) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (!ntf.isRead) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(androidx.compose.foundation.shape.CircleShape)
                                            .background(badgeColor)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = ntf.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = TextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = ntf.message,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = TextSecondary
                            )
                        }

                        CyberBadge(text = ntf.type.label, color = badgeColor)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// 3. ACTIVITY LOG SCREEN
@Composable
fun ActivityLogScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val logs by viewModel.activityLogs.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        CyberCard(
            borderColor = NeonGreen,
            backgroundColor = CyberBackgroundSecondary
        ) {
            Text(
                text = "IMMUTABLE AUDIT TRAIL",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = NeonGreen
                )
            )
            Text(
                text = "EVERY CRUD OPERATION & METRIC TRANSITION LOGGED",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    color = TextMuted
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(logs, key = { it.id }) { log ->
                val actionColor = when (log.action.uppercase()) {
                    "CREATED" -> NeonGreen
                    "DELETED" -> NeonRed
                    "COMPLETED" -> NeonGreen
                    "STATUS_CHANGED" -> NeonCyan
                    "RECORDED" -> NeonPurple
                    else -> NeonAmber
                }

                CyberCard(
                    borderColor = CyberBorder,
                    backgroundColor = CyberSurface,
                    cornerPadding = 8.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CyberBadge(text = log.action, color = actionColor)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = log.entityType.uppercase(),
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextMuted
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = log.entityTitle,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = TextPrimary
                            )
                        }

                        Text(
                            text = log.userName.take(10),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = TextSecondary
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
}

// 4. SETTINGS & INTEGRATIONS SCREEN
@Composable
fun SettingsScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.currentUser.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val timezone by viewModel.timezone.collectAsState()
    val sheetsConnected by viewModel.googleSheetsConnected.collectAsState()
    val calendarConnected by viewModel.googleCalendarConnected.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            // Profile Card
            CyberCard(
                borderColor = NeonGreen,
                backgroundColor = CyberBackgroundSecondary
            ) {
                Text(
                    text = "OPERATOR CREDENTIALS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = userProfile.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                Text(
                    text = "${userProfile.email} // Role: ${userProfile.role.label}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Integrations
        item {
            CyberCard(
                borderColor = CyberBorder,
                backgroundColor = CyberSurface
            ) {
                Text(
                    text = "EXTERNAL GOOGLE INTEGRATIONS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Google Sheets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Google Sheets Auto-Sync", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                        Text("Bi-directional task rows mirror", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = TextMuted)
                    }
                    Switch(
                        checked = sheetsConnected,
                        onCheckedChange = { viewModel.toggleGoogleSheets() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = NeonGreen
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = CyberBorderSubtle)
                Spacer(modifier = Modifier.height(8.dp))

                // Google Calendar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Google Calendar Sync", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                        Text("Chrono deadlines & scheduled meetings", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = TextMuted)
                    }
                    Switch(
                        checked = calendarConnected,
                        onCheckedChange = { viewModel.toggleGoogleCalendar() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = NeonCyan
                        )
                    )
                }
            }
        }

        // Application Settings: Currency, Timezone
        item {
            CyberCard(
                borderColor = CyberBorder,
                backgroundColor = CyberSurface
            ) {
                Text(
                    text = "SYSTEM REGIONAL CONFIGURATION",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Currency selector
                Text("Default Ledger Currency", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("BDT", "USD", "EUR").forEach { curr ->
                        val isSel = currency == curr
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSel) NeonGreen.copy(alpha = 0.2f) else CyberSurfaceElevated)
                                .border(1.dp, if (isSel) NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                                .clickable { viewModel.setCurrency(curr) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(curr, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (isSel) NeonGreen else TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Timezone
                Text("Command Timezone", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Asia/Dhaka", "UTC", "America/New_York").forEach { tz ->
                        val isSel = timezone == tz
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSel) NeonCyan.copy(alpha = 0.2f) else CyberSurfaceElevated)
                                .border(1.dp, if (isSel) NeonCyan else CyberBorder, RoundedCornerShape(4.dp))
                                .clickable { viewModel.setTimezone(tz) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tz.take(10), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 9.sp, color = if (isSel) NeonCyan else TextSecondary)
                        }
                    }
                }
            }
        }

        // Data Backup & Export Section
        item {
            CyberCard(
                borderColor = CyberBorder,
                backgroundColor = CyberSurface
            ) {
                Text(
                    text = "DATA BACKUP & RECOVERY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Single Source of Truth JSON Export / Snapshot",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CyberButton(
                        text = "EXPORT JSON",
                        onClick = { viewModel.clearFeedback() }
                    )
                    Button(
                        onClick = { viewModel.clearFeedback() },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceElevated),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
                    ) {
                        Text("RESTORE SNAPSHOT", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = NeonCyan)
                    }
                }
            }
        }

        // Logout
        item {
            CyberButton(
                text = "LOGOUT & TERMINATE SESSION",
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                isPrimary = false
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// 5. AUTH / LOGIN TERMINAL SCREEN
@Composable
fun AuthScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("a.h.rakib94@nexus.os") }
    var password by remember { mutableStateOf("••••••••••••") }
    var selectedRole by remember { mutableStateOf(UserRole.ADMIN) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        CyberCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = NeonGreen,
            backgroundColor = CyberBackgroundSecondary
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "NEXUS OS",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen,
                        letterSpacing = 2.sp
                    )
                )

                Text(
                    text = "PERSONAL COMMAND CENTER // SECURE ACCESS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Command Identifier / Email", fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                    modifier = Modifier.fillMaxWidth().testTag("auth_email_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Access Key / Password", fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                    modifier = Modifier.fillMaxWidth().testTag("auth_password_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonGreen,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "SELECT ACCESS LEVEL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    UserRole.values().forEach { role ->
                        val isSel = selectedRole == role
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSel) NeonGreen.copy(alpha = 0.2f) else CyberSurface)
                                .border(1.dp, if (isSel) NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                                .clickable { selectedRole = role }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = role.label.uppercase(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) NeonGreen else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                CyberButton(
                    text = "INITIALIZE TERMINAL SESSION",
                    onClick = { viewModel.login(email, selectedRole) },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "login_button"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "SESSION PROTOCOL: AES-256 // ZERO TRUST GATEWAY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.sp,
                        color = TextMuted
                    )
                )
            }
        }
    }
}
