package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.NexusRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class NexusScreen(val label: String, val category: String) {
    DASHBOARD("Dashboard", "MAIN CONTROL"),
    TASK_MANAGER("Task Manager", "MAIN CONTROL"),
    TODO_LIST("To-Do List", "MAIN CONTROL"),
    CALENDAR("Calendar", "MAIN CONTROL"),
    ROUTINE_PLANNER("Routine Planner", "MAIN CONTROL"),

    PROJECTS("Projects", "PROJECT MANAGEMENT"),
    RACI_MATRIX("RACI Matrix", "PROJECT MANAGEMENT"),
    KPI_METRICS("KPI & Metrics", "PROJECT MANAGEMENT"),
    GANTT_CHART("Gantt Chart", "PROJECT MANAGEMENT"),

    FOCUS_MODE("Focus Mode", "PERSONAL DEVELOPMENT"),
    VOCABULARY("Vocabulary", "PERSONAL DEVELOPMENT"),

    WALLET("Wallet & Expenses", "FINANCIAL MANAGEMENT"),

    GLOBAL_SEARCH("Global Search", "SYSTEM"),
    NOTIFICATIONS("Notifications", "SYSTEM"),
    ACTIVITY_LOG("Activity Log", "SYSTEM"),
    SETTINGS("Settings", "SYSTEM")
}

data class SearchResultItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: String,
    val targetScreen: NexusScreen
)

class NexusViewModel(
    private val repository: NexusRepository = NexusRepository.getInstance()
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Navigation State
    private val _currentScreen = MutableStateFlow(NexusScreen.DASHBOARD)
    val currentScreen: StateFlow<NexusScreen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<NexusScreen>()

    // Global Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Modals & Dialogs
    val showCreateTaskDialog = MutableStateFlow(false)
    val showCreateProjectDialog = MutableStateFlow(false)
    val showCreateTransactionDialog = MutableStateFlow(false)
    val showCreateRoutineDialog = MutableStateFlow(false)
    val showCreateWordDialog = MutableStateFlow(false)
    val selectedTaskForDetails = MutableStateFlow<TaskItem?>(null)

    // System Feedback Toast
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    // Data streams from repository
    val currentUser = repository.currentUser
    val tasks = repository.tasks
    val projects = repository.projects
    val routines = repository.routines
    val calendarEvents = repository.calendarEvents
    val focusSessions = repository.focusSessions
    val vocabularyWords = repository.vocabularyWords
    val transactions = repository.transactions
    val budgets = repository.budgets
    val notifications = repository.notifications
    val activityLogs = repository.activityLogs
    val currency = repository.currency
    val timezone = repository.timezone
    val googleSheetsConnected = repository.googleSheetsConnected
    val googleCalendarConnected = repository.googleCalendarConnected
    val waterIntakeMl = repository.waterIntakeMl
    val dailyWaterGoalMl = repository.dailyWaterGoalMl

    // Unread Notifications Count
    val unreadNotificationsCount: StateFlow<Int> = notifications.map { list ->
        list.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Search Results
    val searchResults: StateFlow<List<SearchResultItem>> = _searchQuery.map { query ->
        if (query.isBlank()) {
            emptyList()
        } else {
            val q = query.trim().lowercase()
            val results = mutableListOf<SearchResultItem>()

            tasks.value.filter { it.title.lowercase().contains(q) || it.description.lowercase().contains(q) }
                .forEach {
                    results.add(SearchResultItem(it.id, it.title, "Task // ${it.status.label} // ${it.priority.label}", "TASK", NexusScreen.TASK_MANAGER))
                }

            projects.value.filter { it.name.lowercase().contains(q) || it.description.lowercase().contains(q) }
                .forEach {
                    results.add(SearchResultItem(it.id, it.name, "Project // Lead: ${it.lead} // ${it.progressPercent}%", "PROJECT", NexusScreen.PROJECTS))
                }

            routines.value.filter { it.title.lowercase().contains(q) || it.category.lowercase().contains(q) }
                .forEach {
                    results.add(SearchResultItem(it.id, it.title, "Routine // ${it.time} // Streak: ${it.streak}d", "ROUTINE", NexusScreen.ROUTINE_PLANNER))
                }

            vocabularyWords.value.filter { it.englishWord.lowercase().contains(q) || it.bengaliMeaning.lowercase().contains(q) }
                .forEach {
                    results.add(SearchResultItem(it.id, it.englishWord, "Vocab: ${it.bengaliMeaning}", "VOCAB", NexusScreen.VOCABULARY))
                }

            transactions.value.filter { it.description.lowercase().contains(q) || it.category.lowercase().contains(q) }
                .forEach {
                    results.add(SearchResultItem(it.id, it.description, "Tx: ${it.amount} // ${it.category}", "TRANSACTION", NexusScreen.WALLET))
                }

            results
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- NAVIGATION ACTIONS ---
    fun navigateTo(screen: NexusScreen) {
        if (_currentScreen.value != screen) {
            _screenHistory.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        return if (_screenHistory.isNotEmpty()) {
            _currentScreen.value = _screenHistory.removeAt(_screenHistory.size - 1)
            true
        } else false
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearFeedback() {
        _feedbackMessage.value = null
    }

    private fun postFeedback(msg: String) {
        _feedbackMessage.value = msg
    }

    // --- TASK ACTIONS ---
    fun createTask(
        title: String,
        description: String,
        projectId: String,
        priority: TaskPriority,
        deadline: String,
        assignee: String,
        responsible: String,
        accountable: String
    ) {
        repository.addTask(
            title = title,
            description = description,
            projectId = projectId,
            priority = priority,
            deadline = deadline,
            assignee = assignee,
            responsible = responsible,
            accountable = accountable
        )
        postFeedback("TASK // CREATED & SYNCHRONIZED")
    }

    fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        repository.updateTaskStatus(taskId, newStatus)
        postFeedback("TASK // STATUS -> ${newStatus.label.uppercase()}")
    }

    fun updateTask(updated: TaskItem) {
        repository.updateTask(updated)
        postFeedback("TASK // RECORD UPDATED")
    }

    fun deleteTask(taskId: String) {
        repository.deleteTask(taskId)
        postFeedback("TASK // DELETED")
    }

    // --- ROUTINE ACTIONS ---
    fun toggleRoutine(routineId: String) {
        repository.toggleRoutine(routineId)
    }

    fun createRoutine(title: String, description: String, time: String, freq: RoutineFrequency, cat: String) {
        repository.addRoutine(title, description, time, freq, cat)
        postFeedback("ROUTINE // REGISTERED")
    }

    // --- PROJECT ACTIONS ---
    fun createProject(name: String, desc: String, deadline: String, lead: String) {
        repository.addProject(name, desc, deadline, lead)
        postFeedback("PROJECT // REGISTERED")
    }

    // --- FOCUS ACTIONS ---
    fun finishFocusSession(minutes: Int, taskName: String?) {
        repository.recordFocusSession(minutes, taskName)
        postFeedback("FOCUS // +$minutes MINS LOGGED TO DATABASE")
    }

    // --- VOCABULARY ACTIONS ---
    fun toggleWordLearned(id: String) {
        repository.toggleWordLearned(id)
    }

    fun toggleWordFavorite(id: String) {
        repository.toggleWordFavorite(id)
    }

    fun addWord(english: String, bengali: String, example: String, pronunciation: String = "") {
        repository.addVocabularyWord(english, bengali, example, pronunciation)
        postFeedback("VOCABULARY // WORD REGISTERED")
    }

    // --- TRANSACTION ACTIONS ---
    fun addTransaction(
        accountType: AccountType,
        type: TransactionType,
        amount: Double,
        category: String,
        description: String,
        date: String
    ) {
        repository.addTransaction(accountType, type, amount, category, description, date)
        postFeedback("LEDGER // ${type.name} RECORDED")
    }

    // --- NOTIFICATION ACTIONS ---
    fun markNotificationRead(id: String) {
        repository.markNotificationRead(id)
    }

    fun markAllNotificationsRead() {
        repository.markAllNotificationsRead()
        postFeedback("NOTIFICATIONS // ALL CLEARED")
    }

    // --- SETTINGS ACTIONS ---
    fun setCurrency(curr: String) {
        repository.setCurrency(curr)
        postFeedback("CURRENCY SET TO $curr")
    }

    fun setTimezone(tz: String) {
        repository.setTimezone(tz)
        postFeedback("TIMEZONE SET TO $tz")
    }

    fun toggleGoogleSheets() {
        repository.toggleGoogleSheetsSync()
    }

    fun toggleGoogleCalendar() {
        repository.toggleGoogleCalendarSync()
    }

    fun login(email: String, role: UserRole) {
        repository.login(email, role)
        postFeedback("ACCESS GRANTED // ${role.label.uppercase()}")
    }

    fun logout() {
        repository.logout()
        postFeedback("SESSION DISCONNECTED")
    }

    // --- HYDRATION ACTIONS ---
    fun logWaterIntake(ml: Int) {
        repository.addWaterIntake(ml)
        postFeedback("HYDRATION // +${ml}ml RECORDED")
    }

    fun resetWaterIntake() {
        repository.resetWaterIntake()
        postFeedback("HYDRATION RESET")
    }
}
