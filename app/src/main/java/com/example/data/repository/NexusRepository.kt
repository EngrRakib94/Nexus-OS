package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class NexusRepository private constructor() {

    companion object {
        @Volatile
        private var instance: NexusRepository? = null

        fun getInstance(): NexusRepository {
            return instance ?: synchronized(this) {
                instance ?: NexusRepository().also { instance = it }
            }
        }
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // 1. User Profile & Auth
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "usr-01",
            name = "Commander Rakib",
            email = "a.h.rakib94@nexus.os",
            role = UserRole.ADMIN,
            avatarInitials = "RK",
            isLoggedIn = true
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // 2. Projects
    private val _projects = MutableStateFlow<List<ProjectItem>>(
        listOf(
            ProjectItem(
                id = "proj-01",
                name = "Cyber Command Architecture",
                description = "Central command core, secure data sync engine and zero-trust protocol.",
                status = ProjectStatus.ACTIVE,
                startDate = "2026-11-01",
                deadline = "2026-12-01",
                lead = "Commander Rakib",
                teamMembers = listOf("Commander Rakib", "Agent-07", "Engineer-X"),
                progressPercent = 68
            ),
            ProjectItem(
                id = "proj-02",
                name = "Autonomous Grid Telemetry",
                description = "Sensor node ingestion, telemetry data stream, and quantum encryption pipeline.",
                status = ProjectStatus.ACTIVE,
                startDate = "2026-11-05",
                deadline = "2026-12-20",
                lead = "Engineer-X",
                teamMembers = listOf("Engineer-X", "Commander Rakib", "QA-Lead"),
                progressPercent = 42
            ),
            ProjectItem(
                id = "proj-03",
                name = "Financial Security Matrix",
                description = "Autonomous treasury tracking, multi-account ledger, and expense compliance engine.",
                status = ProjectStatus.PLANNING,
                startDate = "2026-11-15",
                deadline = "2026-12-30",
                lead = "Commander Rakib",
                teamMembers = listOf("Commander Rakib", "Fin-Specialist"),
                progressPercent = 15
            )
        )
    )
    val projects: StateFlow<List<ProjectItem>> = _projects.asStateFlow()

    // 3. Central Tasks
    private val _tasks = MutableStateFlow<List<TaskItem>>(
        listOf(
            TaskItem(
                id = "tsk-101",
                title = "Design Central Database Schema",
                description = "Implement unified single source of truth across all command modules.",
                projectId = "proj-01",
                projectName = "Cyber Command Architecture",
                status = TaskStatus.COMPLETED,
                priority = TaskPriority.CRITICAL,
                startDate = "2026-11-02",
                deadline = "2026-11-10",
                assignee = "Commander Rakib",
                responsible = "Commander Rakib",
                accountable = "Lead Architect",
                consulted = "Database Admin",
                informed = "All Nodes",
                durationDays = 8,
                progressPercent = 100
            ),
            TaskItem(
                id = "tsk-102",
                title = "Build Cyberpunk Command Center HUD",
                description = "Implement translucent panels, neon telemetry widgets, and real-time feeds.",
                projectId = "proj-01",
                projectName = "Cyber Command Architecture",
                status = TaskStatus.IN_PROGRESS,
                priority = TaskPriority.HIGH,
                startDate = "2026-11-08",
                deadline = "2026-11-16",
                assignee = "Commander Rakib",
                responsible = "Commander Rakib",
                accountable = "Commander Rakib",
                consulted = "UX Specialist",
                informed = "Dev Unit",
                durationDays = 8,
                progressPercent = 75
            ),
            TaskItem(
                id = "tsk-103",
                title = "Implement RACI Matrix Engine",
                description = "Enforce single accountable role, workload telemetry and cross-module link.",
                projectId = "proj-01",
                projectName = "Cyber Command Architecture",
                status = TaskStatus.IN_PROGRESS,
                priority = TaskPriority.MEDIUM,
                startDate = "2026-11-10",
                deadline = "2026-11-18",
                assignee = "Engineer-X",
                responsible = "Engineer-X",
                accountable = "Commander Rakib",
                consulted = "Manager Ops",
                informed = "Team",
                durationDays = 8,
                progressPercent = 50
            ),
            TaskItem(
                id = "tsk-104",
                title = "Telemetry Data Ingestion Pipeline",
                description = "Construct asynchronous stream ingestion with buffered failover.",
                projectId = "proj-02",
                projectName = "Autonomous Grid Telemetry",
                status = TaskStatus.TODO,
                priority = TaskPriority.HIGH,
                startDate = "2026-11-14",
                deadline = "2026-11-22",
                assignee = "Agent-07",
                responsible = "Agent-07",
                accountable = "Engineer-X",
                consulted = "Security Officer",
                informed = "System Leads",
                durationDays = 8,
                progressPercent = 0
            ),
            TaskItem(
                id = "tsk-105",
                title = "Audit Financial Ledger Entries",
                description = "Reconcile corporate BDT reserves with monthly operational budget limits.",
                projectId = "proj-03",
                projectName = "Financial Security Matrix",
                status = TaskStatus.PENDING,
                priority = TaskPriority.MEDIUM,
                startDate = "2026-11-12",
                deadline = "2026-11-25",
                assignee = "Commander Rakib",
                responsible = "Commander Rakib",
                accountable = "Commander Rakib",
                consulted = "Auditor Alpha",
                informed = "Stakeholders",
                durationDays = 13,
                progressPercent = 20
            ),
            TaskItem(
                id = "tsk-106",
                title = "Gantt Timeline Dependency Validator",
                description = "Validate chronological sequencing and milestone dependencies.",
                projectId = "proj-01",
                projectName = "Cyber Command Architecture",
                status = TaskStatus.TODO,
                priority = TaskPriority.LOW,
                startDate = "2026-11-17",
                deadline = "2026-11-28",
                assignee = "QA-Lead",
                responsible = "QA-Lead",
                accountable = "Lead Architect",
                consulted = "Commander Rakib",
                informed = "Dev Unit",
                durationDays = 11,
                progressPercent = 0
            )
        )
    )
    val tasks: StateFlow<List<TaskItem>> = _tasks.asStateFlow()

    // 4. Routines
    private val _routines = MutableStateFlow<List<RoutineItem>>(
        listOf(
            RoutineItem(
                id = "rtn-01",
                title = "Morning Cyber Scan & Protocol Review",
                description = "Inspect active cluster alarms, security tokens, and server health.",
                time = "07:30 AM",
                frequency = RoutineFrequency.DAILY,
                category = "Operations",
                streak = 9,
                isCompletedToday = true
            ),
            RoutineItem(
                id = "rtn-02",
                title = "Code Architecture & Deep Work Session",
                description = "90 minutes of zero-distraction system coding and review.",
                time = "09:00 AM",
                frequency = RoutineFrequency.DAILY,
                category = "Productivity",
                streak = 6,
                isCompletedToday = true
            ),
            RoutineItem(
                id = "rtn-03",
                title = "English Vocabulary Exploration",
                description = "Master 2 high-level English words and their Bengali nuances.",
                time = "02:00 PM",
                frequency = RoutineFrequency.DAILY,
                category = "Learning",
                streak = 14,
                isCompletedToday = false
            ),
            RoutineItem(
                id = "rtn-04",
                title = "Weekly RACI & Sprint Alignment",
                description = "Reconcile accountabilities and milestone targets with units.",
                time = "05:00 PM",
                frequency = RoutineFrequency.WEEKLY,
                category = "Project",
                streak = 3,
                isCompletedToday = false
            ),
            RoutineItem(
                id = "rtn-05",
                title = "Monthly Ledger & Budget Audit",
                description = "Review BDT cash flows, operational burn rate, and investment goals.",
                time = "08:00 PM",
                frequency = RoutineFrequency.MONTHLY,
                category = "Finance",
                streak = 2,
                isCompletedToday = false
            )
        )
    )
    val routines: StateFlow<List<RoutineItem>> = _routines.asStateFlow()

    // 5. Calendar Events
    private val _calendarEvents = MutableStateFlow<List<CalendarEventItem>>(
        listOf(
            CalendarEventItem(
                id = "cal-01",
                title = "Sprint Milestone: Cyber HUD Preview",
                date = "2026-11-12",
                time = "10:30 AM",
                category = "Milestone",
                isExternalGoogle = true
            ),
            CalendarEventItem(
                id = "cal-02",
                title = "Security Architecture Deep Dive",
                date = "2026-11-14",
                time = "03:00 PM",
                category = "Meeting",
                isExternalGoogle = false
            ),
            CalendarEventItem(
                id = "cal-03",
                title = "Deadline: Cyber Command Architecture HUD",
                date = "2026-11-16",
                time = "06:00 PM",
                category = "Task",
                linkedTaskId = "tsk-102"
            ),
            CalendarEventItem(
                id = "cal-04",
                title = "Telemetry Integration Test",
                date = "2026-11-18",
                time = "11:00 AM",
                category = "Task",
                linkedTaskId = "tsk-103"
            )
        )
    )
    val calendarEvents: StateFlow<List<CalendarEventItem>> = _calendarEvents.asStateFlow()

    // 6. Focus Sessions
    private val _focusSessions = MutableStateFlow<List<FocusSessionRecord>>(
        listOf(
            FocusSessionRecord("foc-01", "Design Central Database Schema", 45, System.currentTimeMillis() - 86400000L),
            FocusSessionRecord("foc-02", "Build Cyberpunk Command Center HUD", 25, System.currentTimeMillis() - 3600000L * 4),
            FocusSessionRecord("foc-03", "Build Cyberpunk Command Center HUD", 45, System.currentTimeMillis() - 3600000L * 2)
        )
    )
    val focusSessions: StateFlow<List<FocusSessionRecord>> = _focusSessions.asStateFlow()

    // 7. Vocabulary
    private val _vocabularyWords = MutableStateFlow<List<VocabularyWord>>(
        listOf(
            VocabularyWord(
                id = "voc-01",
                englishWord = "Resilience",
                pronunciation = "/rɪˈzɪl.jəns/",
                bengaliMeaning = "সহনশীলতা / প্রতিকূলতা কাটিয়ে ওঠার ক্ষমতা",
                exampleSentence = "The distributed system demonstrated remarkable resilience during server outage.",
                isLearned = true,
                isFavorite = true
            ),
            VocabularyWord(
                id = "voc-02",
                englishWord = "Ubiquitous",
                pronunciation = "/juːˈbɪk.wə.təs/",
                bengaliMeaning = "সর্বব্যাপী / সর্বত্র বিদ্যমান",
                exampleSentence = "Cyberpunk interfaces envision ubiquitous connectivity across all life aspects.",
                isLearned = false,
                isFavorite = true
            ),
            VocabularyWord(
                id = "voc-03",
                englishWord = "Ecosystem",
                pronunciation = "/ˈiː.koʊˌsɪs.təm/",
                bengaliMeaning = "বাস্তুতন্ত্র / সমন্বিত কর্মপরিবেশ",
                exampleSentence = "NEXUS OS establishes an integrated command ecosystem.",
                isLearned = true,
                isFavorite = false
            ),
            VocabularyWord(
                id = "voc-04",
                englishWord = "Ephemeral",
                pronunciation = "/ɪˈfem.ər.əl/",
                bengaliMeaning = "ক্ষণস্থায়ী / স্বল্পকালব্যাপী",
                exampleSentence = "Uncached operational telemetry logs are ephemeral by design.",
                isLearned = false,
                isFavorite = false
            ),
            VocabularyWord(
                id = "voc-05",
                englishWord = "Synergy",
                pronunciation = "/ˈsɪn.ɚ.dʒi/",
                bengaliMeaning = "সমষ্টিগত শক্তি / সমন্বিত কার্যকারিতা",
                exampleSentence = "Cross-module data binding yields unprecedented operational synergy.",
                isLearned = true,
                isFavorite = true
            )
        )
    )
    val vocabularyWords: StateFlow<List<VocabularyWord>> = _vocabularyWords.asStateFlow()

    // 8. Financial Transactions
    private val _transactions = MutableStateFlow<List<FinancialTransaction>>(
        listOf(
            FinancialTransaction(
                id = "tx-01",
                accountType = AccountType.BUSINESS,
                type = TransactionType.INCOME,
                amount = 125000.0,
                category = "Client Contract",
                description = "Enterprise Security Audit Milestone 1",
                date = "2026-11-03"
            ),
            FinancialTransaction(
                id = "tx-02",
                accountType = AccountType.BUSINESS,
                type = TransactionType.EXPENSE,
                amount = 18500.0,
                category = "Cloud Servers",
                description = "Google Cloud & Asia-East1 cluster compute instances",
                date = "2026-11-05"
            ),
            FinancialTransaction(
                id = "tx-03",
                accountType = AccountType.PERSONAL,
                type = TransactionType.INCOME,
                amount = 75000.0,
                category = "Salary",
                description = "Monthly Strategic Director Disbursal",
                date = "2026-11-01"
            ),
            FinancialTransaction(
                id = "tx-04",
                accountType = AccountType.PERSONAL,
                type = TransactionType.EXPENSE,
                amount = 9200.0,
                category = "Tech Gear",
                description = "Mechanical low-profile ergonomic matrix keyboard",
                date = "2026-11-07"
            ),
            FinancialTransaction(
                id = "tx-05",
                accountType = AccountType.PERSONAL,
                type = TransactionType.EXPENSE,
                amount = 4500.0,
                category = "Living & Utilities",
                description = "High-speed gigabit fiber terminal link",
                date = "2026-11-10"
            )
        )
    )
    val transactions: StateFlow<List<FinancialTransaction>> = _transactions.asStateFlow()

    // 9. Budgets
    private val _budgets = MutableStateFlow<List<CategoryBudget>>(
        listOf(
            CategoryBudget("bg-01", AccountType.PERSONAL, "Tech Gear", 15000.0, 9200.0),
            CategoryBudget("bg-02", AccountType.PERSONAL, "Living & Utilities", 12000.0, 4500.0),
            CategoryBudget("bg-03", AccountType.BUSINESS, "Cloud Servers", 30000.0, 18500.0),
            CategoryBudget("bg-04", AccountType.BUSINESS, "Tooling & Software", 20000.0, 8500.0)
        )
    )
    val budgets: StateFlow<List<CategoryBudget>> = _budgets.asStateFlow()

    // 10. Notifications
    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "ntf-01",
                title = "Critical Deadline Imminent",
                message = "Task 'Build Cyberpunk Command Center HUD' is due in 4 days.",
                type = NotificationType.DEADLINE,
                isRead = false
            ),
            NotificationItem(
                id = "ntf-02",
                title = "Routine Alert",
                message = "Daily 'English Vocabulary Exploration' is pending execution.",
                type = NotificationType.ROUTINE,
                isRead = false
            ),
            NotificationItem(
                id = "ntf-03",
                title = "Budget Telemetry Warning",
                message = "Tech Gear budget is at 61% of monthly allocated limit.",
                type = NotificationType.BUDGET,
                isRead = true
            ),
            NotificationItem(
                id = "ntf-04",
                title = "Cloud Node Synchronized",
                message = "Google Sheets & Calendar synchronization bridge active.",
                type = NotificationType.SYSTEM,
                isRead = true
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // 11. Activity Log
    private val _activityLogs = MutableStateFlow<List<ActivityLogItem>>(
        listOf(
            ActivityLogItem("log-01", "Commander Rakib", "CREATED", "Task", "Design Central Database Schema"),
            ActivityLogItem("log-02", "Commander Rakib", "COMPLETED", "Task", "Design Central Database Schema"),
            ActivityLogItem("log-03", "Commander Rakib", "UPDATED", "Status", "Build Cyberpunk Command Center HUD -> In Progress"),
            ActivityLogItem("log-04", "Commander Rakib", "LOGGED", "Expense", "Cloud Servers: 18,500 BDT"),
            ActivityLogItem("log-05", "Commander Rakib", "COMPLETED", "Focus", "45 mins deep work on Central Architecture")
        )
    )
    val activityLogs: StateFlow<List<ActivityLogItem>> = _activityLogs.asStateFlow()

    // 12. App Settings State
    private val _currency = MutableStateFlow("BDT")
    val currency: StateFlow<String> = _currency.asStateFlow()

    private val _timezone = MutableStateFlow("Asia/Dhaka")
    val timezone: StateFlow<String> = _timezone.asStateFlow()

    private val _googleSheetsConnected = MutableStateFlow(true)
    val googleSheetsConnected: StateFlow<Boolean> = _googleSheetsConnected.asStateFlow()

    private val _googleCalendarConnected = MutableStateFlow(true)
    val googleCalendarConnected: StateFlow<Boolean> = _googleCalendarConnected.asStateFlow()

    // 13. Hydration & Drinking Water Telemetry
    private val _waterIntakeMl = MutableStateFlow(1250)
    val waterIntakeMl: StateFlow<Int> = _waterIntakeMl.asStateFlow()

    private val _dailyWaterGoalMl = MutableStateFlow(2500)
    val dailyWaterGoalMl: StateFlow<Int> = _dailyWaterGoalMl.asStateFlow()

    // ==========================================
    // UNIFIED MUTATIONS & CROSS-MODULE SYNC
    // ==========================================

    fun login(email: String, role: UserRole) {
        _currentUser.value = _currentUser.value.copy(
            email = email,
            name = if (email.contains("@")) email.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() } else "Commander Rakib",
            role = role,
            isLoggedIn = true
        )
        logActivity("AUTHENTICATED", "Session", "Role: ${role.label}")
    }

    fun logout() {
        _currentUser.value = _currentUser.value.copy(isLoggedIn = false)
        logActivity("TERMINATED", "Session", "User signed out")
    }

    fun setCurrency(newCurrency: String) {
        _currency.value = newCurrency
        logActivity("UPDATED", "Settings", "Default currency changed to $newCurrency")
    }

    fun setTimezone(newTz: String) {
        _timezone.value = newTz
        logActivity("UPDATED", "Settings", "Timezone changed to $newTz")
    }

    fun toggleGoogleSheetsSync() {
        val newState = !_googleSheetsConnected.value
        _googleSheetsConnected.value = newState
        logActivity("SYNC_STATUS", "Google Sheets", if (newState) "Connected" else "Disconnected")
    }

    fun toggleGoogleCalendarSync() {
        val newState = !_googleCalendarConnected.value
        _googleCalendarConnected.value = newState
        logActivity("SYNC_STATUS", "Google Calendar", if (newState) "Connected" else "Disconnected")
    }

    // --- TASK MUTATIONS ---
    fun addTask(
        title: String,
        description: String,
        projectId: String,
        priority: TaskPriority,
        deadline: String,
        assignee: String,
        responsible: String,
        accountable: String
    ) {
        val project = _projects.value.find { it.id == projectId }
        val taskId = "tsk-" + System.currentTimeMillis().toString().takeLast(4)
        val newTask = TaskItem(
            id = taskId,
            title = title,
            description = description,
            projectId = projectId,
            projectName = project?.name ?: "General Operations",
            status = TaskStatus.TODO,
            priority = priority,
            startDate = dateFormat.format(Date()),
            deadline = deadline,
            assignee = assignee,
            responsible = responsible.ifBlank { assignee },
            accountable = accountable.ifBlank { "Lead Architect" },
            consulted = "Advisory Unit",
            informed = "Ops Channel",
            durationDays = 7,
            progressPercent = 0
        )
        _tasks.value = listOf(newTask) + _tasks.value

        // UNIFIED SYNC 1: Automatically add calendar event if deadline provided
        if (deadline.isNotBlank()) {
            val event = CalendarEventItem(
                id = "cal-" + System.currentTimeMillis().toString().takeLast(4),
                title = "Deadline: $title",
                date = deadline,
                time = "06:00 PM",
                category = "Task",
                linkedTaskId = taskId
            )
            _calendarEvents.value = listOf(event) + _calendarEvents.value
        }

        // UNIFIED SYNC 2: Log Activity
        logActivity("CREATED", "Task", title)

        // UNIFIED SYNC 3: Recheck Project Progress
        recalcProjectProgress(projectId)
    }

    fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        var affectedProjectId: String? = null
        var taskTitle = ""
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                affectedProjectId = task.projectId
                taskTitle = task.title
                val newProgress = when (newStatus) {
                    TaskStatus.COMPLETED -> 100
                    TaskStatus.IN_PROGRESS -> if (task.progressPercent == 0) 30 else task.progressPercent
                    TaskStatus.TODO -> 0
                    else -> task.progressPercent
                }
                task.copy(status = newStatus, progressPercent = newProgress)
            } else task
        }

        logActivity("STATUS_CHANGED", "Task", "$taskTitle -> ${newStatus.label}")
        affectedProjectId?.let { recalcProjectProgress(it) }
    }

    fun updateTask(updated: TaskItem) {
        _tasks.value = _tasks.value.map { if (it.id == updated.id) updated else it }
        // Update calendar event if present
        _calendarEvents.value = _calendarEvents.value.map { event ->
            if (event.linkedTaskId == updated.id) {
                event.copy(title = "Deadline: ${updated.title}", date = updated.deadline)
            } else event
        }
        logActivity("UPDATED", "Task", updated.title)
        recalcProjectProgress(updated.projectId)
    }

    fun deleteTask(taskId: String) {
        val task = _tasks.value.find { it.id == taskId }
        _tasks.value = _tasks.value.filterNot { it.id == taskId }
        _calendarEvents.value = _calendarEvents.value.filterNot { it.linkedTaskId == taskId }
        task?.let {
            logActivity("DELETED", "Task", it.title)
            recalcProjectProgress(it.projectId)
        }
    }

    // --- PROJECT MUTATIONS ---
    fun addProject(name: String, description: String, deadline: String, lead: String) {
        val projId = "proj-" + System.currentTimeMillis().toString().takeLast(4)
        val newProject = ProjectItem(
            id = projId,
            name = name,
            description = description,
            status = ProjectStatus.ACTIVE,
            startDate = dateFormat.format(Date()),
            deadline = deadline,
            lead = lead,
            teamMembers = listOf(lead, "Commander Rakib"),
            progressPercent = 0
        )
        _projects.value = _projects.value + newProject
        logActivity("CREATED", "Project", name)
    }

    private fun recalcProjectProgress(projectId: String) {
        val projectTasks = _tasks.value.filter { it.projectId == projectId }
        if (projectTasks.isEmpty()) return
        val completedCount = projectTasks.count { it.status == TaskStatus.COMPLETED }
        val newProgress = (completedCount * 100) / projectTasks.size
        _projects.value = _projects.value.map {
            if (it.id == projectId) it.copy(progressPercent = newProgress) else it
        }
    }

    // --- ROUTINE MUTATIONS ---
    fun toggleRoutine(routineId: String) {
        _routines.value = _routines.value.map { routine ->
            if (routine.id == routineId) {
                val newCompleted = !routine.isCompletedToday
                val newStreak = if (newCompleted) routine.streak + 1 else (routine.streak - 1).coerceAtLeast(0)
                logActivity(if (newCompleted) "COMPLETED" else "UNCHECKED", "Routine", routine.title)
                routine.copy(isCompletedToday = newCompleted, streak = newStreak)
            } else routine
        }
    }

    fun addRoutine(title: String, description: String, time: String, frequency: RoutineFrequency, category: String) {
        val routineId = "rtn-" + System.currentTimeMillis().toString().takeLast(4)
        val newRoutine = RoutineItem(
            id = routineId,
            title = title,
            description = description,
            time = time,
            frequency = frequency,
            category = category,
            streak = 0,
            isCompletedToday = false
        )
        _routines.value = _routines.value + newRoutine
        logActivity("CREATED", "Routine", title)
    }

    // --- CALENDAR MUTATIONS ---
    fun addCalendarEvent(title: String, date: String, time: String, category: String) {
        val eventId = "cal-" + System.currentTimeMillis().toString().takeLast(4)
        val event = CalendarEventItem(
            id = eventId,
            title = title,
            date = date,
            time = time,
            category = category,
            isExternalGoogle = false
        )
        _calendarEvents.value = listOf(event) + _calendarEvents.value
        logActivity("CREATED", "Calendar Event", title)
    }

    // --- FOCUS MODE MUTATIONS ---
    fun recordFocusSession(minutes: Int, taskName: String?) {
        val session = FocusSessionRecord(
            id = "foc-" + System.currentTimeMillis().toString().takeLast(4),
            taskName = taskName,
            durationMinutes = minutes,
            completedAt = System.currentTimeMillis()
        )
        _focusSessions.value = listOf(session) + _focusSessions.value
        logActivity("LOGGED", "Focus Session", "$minutes mins focused${if (!taskName.isNullOrBlank()) " on $taskName" else ""}")
    }

    // --- VOCABULARY MUTATIONS ---
    fun toggleWordLearned(wordId: String) {
        _vocabularyWords.value = _vocabularyWords.value.map {
            if (it.id == wordId) it.copy(isLearned = !it.isLearned) else it
        }
    }

    fun toggleWordFavorite(wordId: String) {
        _vocabularyWords.value = _vocabularyWords.value.map {
            if (it.id == wordId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun addVocabularyWord(english: String, bengali: String, example: String, pronunciation: String = "") {
        val wordId = "voc-" + System.currentTimeMillis().toString().takeLast(4)
        val newWord = VocabularyWord(
            id = wordId,
            englishWord = english,
            bengaliMeaning = bengali,
            exampleSentence = example,
            pronunciation = pronunciation
        )
        _vocabularyWords.value = listOf(newWord) + _vocabularyWords.value
        logActivity("ADDED", "Vocabulary", english)
    }

    // --- FINANCIAL MUTATIONS ---
    fun addTransaction(
        accountType: AccountType,
        type: TransactionType,
        amount: Double,
        category: String,
        description: String,
        date: String
    ) {
        val txId = "tx-" + System.currentTimeMillis().toString().takeLast(4)
        val tx = FinancialTransaction(
            id = txId,
            accountType = accountType,
            type = type,
            amount = amount,
            category = category,
            description = description,
            date = date
        )
        _transactions.value = listOf(tx) + _transactions.value

        // Check against category budgets if expense
        if (type == TransactionType.EXPENSE) {
            _budgets.value = _budgets.value.map { budget ->
                if (budget.accountType == accountType && budget.category.equals(category, ignoreCase = true)) {
                    val newSpent = budget.currentSpent + amount
                    if (newSpent > budget.monthlyLimit) {
                        pushNotification(
                            "Budget Limit Exceeded!",
                            "${budget.category} ($accountType) reached ${newSpent.toInt()} ${_currency.value} (limit: ${budget.monthlyLimit.toInt()})",
                            NotificationType.BUDGET
                        )
                    }
                    budget.copy(currentSpent = newSpent)
                } else budget
            }
        }

        logActivity("RECORDED", "Transaction", "$type: $amount ${_currency.value} ($category)")
    }

    // --- NOTIFICATION MUTATIONS ---
    fun pushNotification(title: String, message: String, type: NotificationType) {
        val ntfId = "ntf-" + System.currentTimeMillis().toString().takeLast(4)
        val newNtf = NotificationItem(
            id = ntfId,
            title = title,
            message = message,
            type = type,
            isRead = false
        )
        _notifications.value = listOf(newNtf) + _notifications.value
    }

    fun markNotificationRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    // --- HYDRATION MUTATIONS ---
    fun addWaterIntake(amountMl: Int) {
        val newTotal = _waterIntakeMl.value + amountMl
        _waterIntakeMl.value = newTotal
        logActivity("HYDRATION", "Health", "+$amountMl ml consumed ($newTotal / ${_dailyWaterGoalMl.value} ml)")
        if (newTotal >= _dailyWaterGoalMl.value && _waterIntakeMl.value - amountMl < _dailyWaterGoalMl.value) {
            pushNotification(
                "Hydration Goal Fulfilled! 💧",
                "Daily water quota of ${_dailyWaterGoalMl.value} ml achieved. Optimal biometric equilibrium maintained.",
                NotificationType.SYSTEM
            )
        }
    }

    fun resetWaterIntake() {
        _waterIntakeMl.value = 0
        logActivity("HYDRATION_RESET", "Health", "Hydration log reset for new cycle")
    }

    // --- ACTIVITY LOG HELPER ---
    private fun logActivity(action: String, entityType: String, title: String) {
        val logId = "log-" + System.currentTimeMillis().toString().takeLast(4)
        val item = ActivityLogItem(
            id = logId,
            userName = _currentUser.value.name,
            action = action,
            entityType = entityType,
            entityTitle = title
        )
        _activityLogs.value = listOf(item) + _activityLogs.value
    }
}
