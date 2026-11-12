package com.example.data.model

enum class UserRole(val label: String) {
    ADMIN("Admin"),
    MANAGER("Manager"),
    MEMBER("Member")
}

data class UserProfile(
    val id: String = "usr-01",
    val name: String = "Commander Rakib",
    val email: String = "a.h.rakib94@nexus.os",
    val role: UserRole = UserRole.ADMIN,
    val avatarInitials: String = "RK",
    val isLoggedIn: Boolean = true
)

enum class TaskStatus(val label: String, val stepIndex: Int) {
    TODO("To Do", 0),
    IN_PROGRESS("In Progress", 1),
    PENDING("Pending", 2),
    COMPLETED("Completed", 3),
    CANCELLED("Cancelled", 4)
}

enum class TaskPriority(val label: String, val level: Int) {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    CRITICAL("Critical", 4)
}

data class TaskItem(
    val id: String,
    val title: String,
    val description: String = "",
    val projectId: String = "",
    val projectName: String = "Core Ops",
    val status: TaskStatus = TaskStatus.TODO,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val startDate: String = "2026-11-12",
    val deadline: String = "2026-11-20",
    val assignee: String = "Rakib",
    val responsible: String = "Rakib",
    val accountable: String = "Lead Architect",
    val consulted: String = "Security Lead",
    val informed: String = "Ops Team",
    val durationDays: Int = 5,
    val progressPercent: Int = 0,
    val dependencyTaskId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
)

enum class ProjectStatus(val label: String) {
    PLANNING("Planning"),
    ACTIVE("Active"),
    ON_HOLD("On Hold"),
    COMPLETED("Completed")
}

data class ProjectItem(
    val id: String,
    val name: String,
    val description: String,
    val status: ProjectStatus = ProjectStatus.ACTIVE,
    val startDate: String = "2026-11-01",
    val deadline: String = "2026-12-15",
    val lead: String = "Rakib",
    val teamMembers: List<String> = listOf("Rakib", "Agent-07", "Engineer-X", "QA-Lead"),
    val progressPercent: Int = 45
)

enum class RoutineFrequency(val label: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly")
}

data class RoutineItem(
    val id: String,
    val title: String,
    val description: String = "",
    val time: String = "08:00 AM",
    val frequency: RoutineFrequency = RoutineFrequency.DAILY,
    val category: String = "Productivity",
    val streak: Int = 5,
    val isCompletedToday: Boolean = false
)

data class CalendarEventItem(
    val id: String,
    val title: String,
    val date: String, // YYYY-MM-DD
    val time: String, // HH:mm
    val category: String, // Task, Sync, Routine, Milestone
    val isExternalGoogle: Boolean = false,
    val linkedTaskId: String? = null
)

data class FocusSessionRecord(
    val id: String,
    val taskName: String?,
    val durationMinutes: Int,
    val completedAt: Long = System.currentTimeMillis()
)

data class VocabularyWord(
    val id: String,
    val englishWord: String,
    val pronunciation: String = "",
    val bengaliMeaning: String,
    val exampleSentence: String,
    val isLearned: Boolean = false,
    val isFavorite: Boolean = false
)

enum class AccountType(val label: String) {
    PERSONAL("Personal"),
    BUSINESS("Business")
}

enum class TransactionType(val label: String) {
    INCOME("Income"),
    EXPENSE("Expense")
}

data class FinancialTransaction(
    val id: String,
    val accountType: AccountType = AccountType.PERSONAL,
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: Double,
    val category: String,
    val description: String,
    val date: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class CategoryBudget(
    val id: String,
    val accountType: AccountType,
    val category: String,
    val monthlyLimit: Double,
    val currentSpent: Double = 0.0
)

enum class NotificationType(val label: String) {
    DEADLINE("Deadline"),
    OVERDUE("Overdue"),
    ROUTINE("Routine"),
    BUDGET("Budget Alert"),
    SYSTEM("System")
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

data class ActivityLogItem(
    val id: String,
    val userName: String = "Commander Rakib",
    val action: String,
    val entityType: String,
    val entityTitle: String,
    val timestamp: Long = System.currentTimeMillis()
)
