package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.data.model.ProjectItem
import com.example.data.model.TaskItem
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ProjectsRaciScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.projects.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    var selectedTab by remember { mutableStateOf("RACI") } // RACI or PROJECTS
    var selectedMemberFilter by remember { mutableStateOf<String?>(null) }
    var showCreateProjectDialog by remember { mutableStateOf(false) }

    val allTeamMembers = remember(tasks, projects) {
        val members = mutableSetOf<String>()
        tasks.forEach {
            members.add(it.responsible)
            members.add(it.accountable)
        }
        projects.forEach { members.addAll(it.teamMembers) }
        members.toList()
    }

    val filteredTasks = tasks.filter { task ->
        selectedMemberFilter == null ||
        task.responsible == selectedMemberFilter ||
        task.accountable == selectedMemberFilter ||
        task.assignee == selectedMemberFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Tab Selector: RACI MATRIX vs PROJECT PORTFOLIO
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("RACI" to "RACI MATRIX", "PROJECTS" to "PROJECTS").forEach { (tabKey, label) ->
                val isSel = selectedTab == tabKey
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSel) NeonGreen.copy(alpha = 0.2f) else CyberSurface)
                        .border(1.dp, if (isSel) NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                        .clickable { selectedTab = tabKey }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) NeonGreen else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedTab == "RACI") {
            // Member Filter pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (selectedMemberFilter == null) NeonCyan.copy(alpha = 0.2f) else CyberSurface)
                        .border(1.dp, if (selectedMemberFilter == null) NeonCyan else CyberBorder, RoundedCornerShape(4.dp))
                        .clickable { selectedMemberFilter = null }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "ALL OPERATIVES",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = if (selectedMemberFilter == null) NeonCyan else TextSecondary
                    )
                }

                allTeamMembers.forEach { member ->
                    val isSel = selectedMemberFilter == member
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSel) NeonCyan.copy(alpha = 0.2f) else CyberSurface)
                            .border(1.dp, if (isSel) NeonCyan else CyberBorder, RoundedCornerShape(4.dp))
                            .clickable { selectedMemberFilter = member }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = member.uppercase(),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = if (isSel) NeonCyan else TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // RACI Legend
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(CyberSurface)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(NeonGreen))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("R: Responsible", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = TextSecondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(NeonRed))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("A: Accountable", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = TextSecondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(NeonCyan))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("C: Consulted", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = TextSecondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(NeonPurple))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("I: Informed", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tasks RACI Matrix List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTasks, key = { it.id }) { task ->
                    CyberCard(
                        borderColor = CyberBorder,
                        backgroundColor = CyberSurface
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                        Text(
                            text = "Project: ${task.projectName}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("RESPONSIBLE (R)", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = NeonGreen)
                                Text(task.responsible, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("ACCOUNTABLE (A)", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = NeonRed)
                                Text(task.accountable, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("CONSULTED (C)", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = NeonCyan)
                                Text(task.consulted, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("INFORMED (I)", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = NeonPurple)
                                Text(task.informed, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else {
            // PROJECTS TAB
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIVE INITIATIVES (${projects.size})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                )

                CyberButton(
                    text = "+ PROJECT",
                    onClick = { showCreateProjectDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(projects, key = { it.id }) { proj ->
                    CyberCard(
                        borderColor = NeonCyan.copy(alpha = 0.5f),
                        backgroundColor = CyberSurface
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = proj.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = TextPrimary
                            )
                            CyberBadge(text = proj.status.label, color = NeonGreen)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = proj.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { proj.progressPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = NeonGreen,
                            trackColor = CyberBorder
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Lead: ${proj.lead}", fontSize = 10.sp, color = NeonCyan, fontFamily = FontFamily.Monospace)
                            Text("Span: ${proj.startDate} -> ${proj.deadline}", fontSize = 10.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    if (showCreateProjectDialog) {
        var name by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        var deadline by remember { mutableStateOf("2026-12-31") }
        var lead by remember { mutableStateOf("Commander Rakib") }

        androidx.compose.ui.window.Dialog(onDismissRequest = { showCreateProjectDialog = false }) {
            CyberCard(
                borderColor = NeonCyan,
                backgroundColor = CyberBackgroundSecondary
            ) {
                Text(
                    text = "INITIATE NEW PROJECT",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Project Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = deadline, onValueChange = { deadline = it }, label = { Text("Deadline (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = lead, onValueChange = { lead = it }, label = { Text("Project Lead") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                CyberButton(
                    text = "LAUNCH INITIATIVE",
                    onClick = {
                        if (name.isNotBlank()) {
                            viewModel.createProject(name, desc, deadline, lead)
                            showCreateProjectDialog = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
