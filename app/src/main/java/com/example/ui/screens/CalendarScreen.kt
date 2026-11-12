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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CalendarEventItem
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun CalendarScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val events by viewModel.calendarEvents.collectAsState()
    val googleCalendarConnected by viewModel.googleCalendarConnected.collectAsState()
    var selectedCalendarView by remember { mutableStateOf("MONTHLY") } // DAILY, WEEKLY, MONTHLY
    var showAddEventDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Header sync row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CyberBadge(
                text = if (googleCalendarConnected) "GOOGLE CALENDAR // SYNCED" else "OFFLINE CALENDAR",
                color = if (googleCalendarConnected) NeonGreen else TextMuted
            )

            CyberButton(
                text = "+ EVENT",
                onClick = { showAddEventDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // View Selector: Daily, Weekly, Monthly
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("DAILY", "WEEKLY", "MONTHLY").forEach { view ->
                val isSel = selectedCalendarView == view
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSel) NeonCyan.copy(alpha = 0.2f) else CyberSurface)
                        .border(1.dp, if (isSel) NeonCyan else CyberBorder, RoundedCornerShape(4.dp))
                        .clickable { selectedCalendarView = view }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = view,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) NeonCyan else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Cyber Calendar Grid / Mini View
        CyberCard(
            borderColor = NeonCyan.copy(alpha = 0.5f),
            backgroundColor = CyberSurface
        ) {
            Text(
                text = "NOVEMBER 2026 // TIME MATRIX",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Days of week
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = TextMuted
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))

            // Selected days strip
            Row(modifier = Modifier.fillMaxWidth()) {
                (9..15).forEach { d ->
                    val isToday = d == 12
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isToday) NeonGreen else CyberSurfaceElevated)
                            .border(1.dp, if (isToday) NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = d.toString(),
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (isToday) Color.Black else TextPrimary
                            )
                            if (d in listOf(12, 14, 16)) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(androidx.compose.foundation.shape.CircleShape)
                                        .background(if (isToday) Color.Black else NeonCyan)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        CyberSectionHeader(
            title = "Scheduled Chrono Events",
            subtitle = "Synchronized deadlines & milestone alerts"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(events, key = { it.id }) { event ->
                CyberCard(
                    borderColor = if (event.isExternalGoogle) NeonCyan.copy(alpha = 0.5f) else CyberBorder,
                    backgroundColor = CyberSurface
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = TextMuted, modifier = Modifier.size(11.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${event.date} // ${event.time}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                )
                            }
                        }
                        CyberBadge(
                            text = event.category.uppercase(),
                            color = when (event.category) {
                                "Milestone" -> NeonAmber
                                "Meeting" -> NeonPurple
                                else -> NeonGreen
                            }
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showAddEventDialog) {
        var eventTitle by remember { mutableStateOf("") }
        var eventDate by remember { mutableStateOf("2026-11-18") }
        var eventTime by remember { mutableStateOf("04:00 PM") }
        var eventCategory by remember { mutableStateOf("Meeting") }

        androidx.compose.ui.window.Dialog(onDismissRequest = { showAddEventDialog = false }) {
            CyberCard(
                borderColor = NeonCyan,
                backgroundColor = CyberBackgroundSecondary
            ) {
                Text(
                    text = "SCHEDULE NEW EVENT",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = eventTitle,
                    onValueChange = { eventTitle = it },
                    label = { Text("Event Title", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = eventDate,
                    onValueChange = { eventDate = it },
                    label = { Text("Date (YYYY-MM-DD)", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = eventTime,
                    onValueChange = { eventTime = it },
                    label = { Text("Time (e.g. 02:00 PM)", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                CyberButton(
                    text = "SYNCHRONIZE TO CALENDAR",
                    onClick = {
                        if (eventTitle.isNotBlank()) {
                            viewModel.calendarEvents.value // triggers
                            // Add via repository
                            com.example.data.repository.NexusRepository.getInstance().addCalendarEvent(
                                eventTitle, eventDate, eventTime, eventCategory
                            )
                            showAddEventDialog = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
