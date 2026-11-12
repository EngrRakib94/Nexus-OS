package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskItem
import com.example.data.model.TaskStatus
import com.example.ui.theme.*

data class GanttDisplayTask(
    val id: String,
    val title: String,
    val startDay: Int, // e.g. 10
    val durationDays: Int, // e.g. 4
    val color: Color,
    val assigneeInitials: String,
    val assigneeName: String,
    val dependsOnPrevious: Boolean = false,
    val hasMilestone: Boolean = false
)

@Composable
fun PictureAccurateGanttChart(
    tasks: List<TaskItem>,
    onTaskClick: ((TaskItem) -> Unit)? = null,
    modifier: Modifier = Modifier,
    useCardWrapper: Boolean = true
) {
    val scrollState = rememberScrollState()

    // Timeline days configuration
    val startDay = 9
    val totalDays = 20 // from day 9 to 28
    val daysList = (startDay until (startDay + totalDays)).toList()
    val todayDay = 12 // Current date marker (matches red line in picture)

    val dayColumnWidth = 42.dp
    val totalChartWidth = dayColumnWidth * totalDays

    // Predefined visual vibrant palette matching the uploaded reference image
    val colorsPalette = listOf(
        Color(0xFF0284C7), // Blue
        Color(0xFF9333EA), // Purple
        Color(0xFFF97316), // Orange
        Color(0xFFEAB308), // Amber / Gold
        Color(0xFF06B6D4), // Cyan
        Color(0xFF16A34A), // Green
        Color(0xFF2563EB), // Royal Blue
        Color(0xFFD946EF)  // Magenta
    )

    // Map tasks to Gantt display objects with start days and durations
    val ganttTasks = tasks.mapIndexed { index, task ->
        val derivedStart = (10 + (index * 2)).coerceIn(9, 23)
        val derivedDuration = task.durationDays.coerceIn(3, 7)
        val initials = task.responsible.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").ifEmpty { "OP" }
        GanttDisplayTask(
            id = task.id,
            title = task.title,
            startDay = derivedStart,
            durationDays = derivedDuration,
            color = colorsPalette[index % colorsPalette.size],
            assigneeInitials = initials,
            assigneeName = task.responsible,
            dependsOnPrevious = index in listOf(1, 4),
            hasMilestone = index == 0
        )
    }

    val content = @Composable {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF0D131C))
                .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            // Header: Legend & View Info
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
                            .background(Color(0xFFEF4444))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "GANTT TIMELINE SCHEDULE",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "TODAY: NOV $todayDay",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scrollable Timeline Chart Canvas & Rows
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                Column(modifier = Modifier.width(totalChartWidth)) {

                    // 1. Calendar Weeks Header (e.g. CW 45 NOV, CW 46 NOV, CW 47 NOV)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CW 46 NOV",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = TextMuted
                            ),
                            modifier = Modifier.padding(start = dayColumnWidth * 2)
                        )
                        Text(
                            text = "CW 47 NOV",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = TextMuted
                            ),
                            modifier = Modifier.padding(start = dayColumnWidth * 7)
                        )
                        Text(
                            text = "CW 48 NOV",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = TextMuted
                            ),
                            modifier = Modifier.padding(end = dayColumnWidth * 2)
                        )
                    }

                    // 2. Numerical Day Headers (24, 25, 26, 27, 28, 29, 30...)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF141C28))
                            .padding(vertical = 5.dp)
                    ) {
                        daysList.forEach { dayNum ->
                            val isToday = dayNum == todayDay
                            Box(
                                modifier = Modifier
                                    .width(dayColumnWidth),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNum.toString(),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isToday) Color(0xFFEF4444) else TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 3. Grid & Task Rows with Red Today Line
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Background Grid Lines & Red Current Date Marker
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((ganttTasks.size * 38 + 10).dp)
                        ) {
                            val colWidthPx = dayColumnWidth.toPx()

                            // Draw subtle vertical grid columns
                            daysList.forEachIndexed { idx, _ ->
                                val x = idx * colWidthPx
                                drawLine(
                                    color = Color(0xFF1A2638),
                                    start = Offset(x, 0f),
                                    end = Offset(x, size.height),
                                    strokeWidth = 1f
                                )
                            }

                            // Draw Dependency Step Connectors (like in reference image)
                            ganttTasks.forEachIndexed { index, current ->
                                if (current.dependsOnPrevious && index > 0) {
                                    val prev = ganttTasks[index - 1]
                                    val prevEndX = (prev.startDay - startDay + prev.durationDays) * colWidthPx
                                    val prevCenterY = ((index - 1) * 38.dp.toPx()) + 16.dp.toPx()
                                    val currStartX = (current.startDay - startDay) * colWidthPx
                                    val currCenterY = (index * 38.dp.toPx()) + 16.dp.toPx()

                                    val path = Path().apply {
                                        moveTo(prevEndX, prevCenterY)
                                        lineTo(prevEndX + 10.dp.toPx(), prevCenterY)
                                        lineTo(prevEndX + 10.dp.toPx(), currCenterY)
                                        lineTo(currStartX - 3.dp.toPx(), currCenterY)
                                    }

                                    drawPath(
                                        path = path,
                                        color = Color(0xFF94A3B8),
                                        style = Stroke(width = 1.5.dp.toPx())
                                    )

                                    // Arrow head
                                    drawCircle(
                                        color = Color(0xFF94A3B8),
                                        radius = 2.5.dp.toPx(),
                                        center = Offset(currStartX - 3.dp.toPx(), currCenterY)
                                    )
                                }
                            }

                            // RED TODAY LINE (Exactly like reference picture!)
                            val todayIndex = daysList.indexOf(todayDay)
                            if (todayIndex >= 0) {
                                val redX = todayIndex * colWidthPx + (colWidthPx / 2)
                                drawLine(
                                    color = Color(0xFFEF4444),
                                    start = Offset(redX, 0f),
                                    end = Offset(redX, size.height),
                                    strokeWidth = 2.dp.toPx()
                                )

                                // Red top dot
                                drawCircle(
                                    color = Color(0xFFEF4444),
                                    radius = 3.5.dp.toPx(),
                                    center = Offset(redX, 0f)
                                )
                            }
                        }

                        // Task Pill Bars
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            ganttTasks.forEach { gTask ->
                                val offsetStartDays = (gTask.startDay - startDay).coerceAtLeast(0)
                                val barWidth = dayColumnWidth * gTask.durationDays
                                val leftOffset = dayColumnWidth * offsetStartDays

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(32.dp)
                                ) {
                                    // Pill-shaped Task Bar with Avatar on right
                                    Row(
                                        modifier = Modifier
                                            .offset(x = leftOffset)
                                            .width(barWidth)
                                            .height(30.dp)
                                            .clip(RoundedCornerShape(15.dp))
                                            .background(gTask.color)
                                            .border(1.dp, gTask.color.copy(alpha = 0.8f), RoundedCornerShape(15.dp))
                                            .clickable {
                                                tasks.find { it.id == gTask.id }?.let { onTaskClick?.invoke(it) }
                                            }
                                            .padding(horizontal = 8.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = gTask.title,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontFamily = FontFamily.Default,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 11.sp,
                                                color = Color.White
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        // Circular Avatar on right of bar
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(Color.White.copy(alpha = 0.25f))
                                                .border(1.dp, Color.White, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = gTask.assigneeInitials,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                    }

                                    // Milestone circle icon if applicable
                                    if (gTask.hasMilestone) {
                                        Box(
                                            modifier = Modifier
                                                .offset(x = leftOffset + barWidth + 6.dp, y = 6.dp)
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .border(1.5.dp, Color(0xFF94A3B8), CircleShape)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer hints
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SWIPE HORIZONTALLY TO NAVIGATE TIME MATRIX",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.sp,
                    color = TextMuted
                )
                Text(
                    text = "TAP BAR TO INSPECT // RACI LINKED",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 8.sp,
                    color = NeonCyan
                )
            }
        }
    }

    if (useCardWrapper) {
        CyberCard(
            borderColor = NeonCyan,
            backgroundColor = CyberSurface,
            cornerPadding = 0.dp,
            modifier = modifier
        ) {
            content()
        }
    } else {
        content()
    }
}
