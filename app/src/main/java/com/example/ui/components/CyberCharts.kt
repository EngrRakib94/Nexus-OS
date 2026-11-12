package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProjectItem
import com.example.data.model.TaskItem
import com.example.data.model.TaskStatus
import com.example.ui.theme.*

// 1. KPI STATUS DISTRIBUTION BAR GRAPH
@Composable
fun CyberKpiBarChart(
    tasks: List<TaskItem>,
    modifier: Modifier = Modifier
) {
    val total = tasks.size.coerceAtLeast(1)
    val completed = tasks.count { it.status == TaskStatus.COMPLETED }
    val inProgress = tasks.count { it.status == TaskStatus.IN_PROGRESS }
    val pending = tasks.count { it.status == TaskStatus.PENDING }
    val todo = tasks.count { it.status == TaskStatus.TODO }

    val data = listOf(
        Triple("COMPLETED", completed, NeonGreen),
        Triple("IN PROGRESS", inProgress, NeonCyan),
        Triple("PENDING", pending, NeonAmber),
        Triple("TO DO", todo, Color(0xFF64748B))
    )

    CyberCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = NeonGreen.copy(alpha = 0.5f),
        backgroundColor = CyberSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "KPI // TASK STATUS DISTRIBUTION GRAPH",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "LIVE METRICS FROM CENTRAL TASK REPOSITORY",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                    color = TextMuted
                )
            }
            CyberBadge(text = "$total TOTAL", color = NeonGreen)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Visual Vertical Bar Chart Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(CyberBackgroundSecondary)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val maxCount = (data.maxOfOrNull { it.second } ?: 1).coerceAtLeast(1)
                val barWidth = 32.dp.toPx()
                val totalBars = data.size
                val spaceBetween = (size.width - (totalBars * barWidth)) / (totalBars + 1)
                val chartHeight = size.height - 18.dp.toPx()

                // Baseline grid
                drawLine(
                    color = CyberBorder,
                    start = Offset(0f, chartHeight),
                    end = Offset(size.width, chartHeight),
                    strokeWidth = 1.5.dp.toPx()
                )

                data.forEachIndexed { index, item ->
                    val (_, count, color) = item
                    val barHeight = (count.toFloat() / maxCount) * (chartHeight - 10.dp.toPx())
                    val xOffset = spaceBetween + index * (barWidth + spaceBetween)
                    val topY = chartHeight - barHeight.coerceAtLeast(6.dp.toPx())

                    // Glowing Bar
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            listOf(color, color.copy(alpha = 0.4f))
                        ),
                        topLeft = Offset(xOffset, topY),
                        size = Size(barWidth, barHeight.coerceAtLeast(6.dp.toPx())),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                }
            }

            // Overlay Bar Labels below canvas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                data.forEach { (label, count, color) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$count",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = color
                        )
                        Text(
                            text = label.take(4),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

// 2. GANTT TIMELINE MINI-GRAPH
@Composable
fun CyberGanttTimelineMiniGraph(
    tasks: List<TaskItem>,
    modifier: Modifier = Modifier
) {
    CyberCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = NeonCyan.copy(alpha = 0.5f),
        backgroundColor = CyberSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "GANTT // TIMELINE SCHEDULE GRAPH",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "ACTIVE TIMELINES & DURATION SPANS",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                    color = TextMuted
                )
            }
            CyberBadge(text = "GANTT VIEW", color = NeonCyan)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Time reference bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(3.dp))
                .background(CyberSurfaceElevated)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("START (NOV 01)", fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
            Text("MID-TERM (NOV 15)", fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = NeonCyan)
            Text("TARGET (NOV 30)", fontSize = 8.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Gantt Schedule Bars
        tasks.take(4).forEach { task ->
            val progress = (task.progressPercent / 100f).coerceIn(0.08f, 1f)
            val barColor = when (task.status) {
                TaskStatus.COMPLETED -> NeonGreen
                TaskStatus.IN_PROGRESS -> NeonCyan
                TaskStatus.PENDING -> NeonAmber
                else -> Color(0xFF64748B)
            }

            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = task.title.take(24),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${task.progressPercent}% // R:${task.responsible.take(6)}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        color = barColor
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyberBorder)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(barColor.copy(alpha = 0.5f), barColor)
                                )
                            )
                    )
                }
            }
        }
    }
}

// 3. RACI WORKLOAD ALLOCATION GRAPH
@Composable
fun CyberRaciWorkloadGraph(
    tasks: List<TaskItem>,
    modifier: Modifier = Modifier
) {
    val memberWorkloads = tasks.groupBy { it.responsible }
    val maxWorkload = (memberWorkloads.maxOfOrNull { it.value.size } ?: 1).coerceAtLeast(1)

    CyberCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = NeonPurple.copy(alpha = 0.5f),
        backgroundColor = CyberSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "RACI // OPERATIVE WORKLOAD GRAPH",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonPurple,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "TASK ASSIGNMENT DISTRIBUTION PER MEMBER",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                    color = TextMuted
                )
            }
            CyberBadge(text = "${memberWorkloads.size} OPERATIVES", color = NeonPurple)
        }

        Spacer(modifier = Modifier.height(10.dp))

        memberWorkloads.forEach { (member, memberTasks) ->
            val count = memberTasks.size
            val fraction = (count.toFloat() / maxWorkload).coerceIn(0.1f, 1f)
            val doneCount = memberTasks.count { it.status == TaskStatus.COMPLETED }
            val activeCount = memberTasks.count { it.status != TaskStatus.COMPLETED }

            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = member,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "$count TASKS ($doneCount DONE, $activeCount ACT)",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp,
                        color = NeonPurple
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyberBorder)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(NeonPurple.copy(alpha = 0.4f), NeonPurple)
                                )
                            )
                    )
                }
            }
        }
    }
}

// 4. PROJECT COMPLETION GAUGE GRAPH
@Composable
fun CyberProjectProgressGraph(
    projects: List<ProjectItem>,
    modifier: Modifier = Modifier
) {
    CyberCard(
        modifier = modifier.fillMaxWidth(),
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
                    text = "PROJECTS // INITIATIVE DELIVERY GAUGES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonAmber,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "PORTFOLIO COMPLETION & DEADLINES",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                    color = TextMuted
                )
            }
            CyberBadge(text = "${projects.size} INITIATIVES", color = NeonAmber)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            projects.forEach { proj ->
                val progress = proj.progressPercent / 100f
                val ringColor = when {
                    proj.progressPercent >= 60 -> NeonGreen
                    proj.progressPercent >= 30 -> NeonCyan
                    else -> NeonAmber
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(CyberSurfaceElevated)
                        .border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier.size(54.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawArc(
                                    color = CyberBorder,
                                    startAngle = -90f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                                )
                                drawArc(
                                    color = ringColor,
                                    startAngle = -90f,
                                    sweepAngle = 360f * progress,
                                    useCenter = false,
                                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            Text(
                                text = "${proj.progressPercent}%",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = ringColor
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = proj.name.take(12),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            color = TextPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = proj.status.label,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 8.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}
