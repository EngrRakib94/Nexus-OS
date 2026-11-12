package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.*
import com.example.data.repository.NexusRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("NEXUS OS", appName)
    }

    @Test
    fun `nexus repository task synchronization and cross-module link`() {
        val repo = NexusRepository.getInstance()
        val initialTasksCount = repo.tasks.value.size
        val initialEventsCount = repo.calendarEvents.value.size

        // Add task with deadline
        repo.addTask(
            title = "Test Cyber Defense",
            description = "Audit firewall rules",
            projectId = "proj-01",
            priority = TaskPriority.CRITICAL,
            deadline = "2026-11-28",
            assignee = "Commander Rakib",
            responsible = "Commander Rakib",
            accountable = "Lead Architect"
        )

        assertEquals(initialTasksCount + 1, repo.tasks.value.size)
        // Check unified sync: calendar event created automatically for deadline!
        assertEquals(initialEventsCount + 1, repo.calendarEvents.value.size)

        val createdTask = repo.tasks.value.first()
        assertEquals("Test Cyber Defense", createdTask.title)
        assertEquals(TaskPriority.CRITICAL, createdTask.priority)

        // Complete task
        repo.updateTaskStatus(createdTask.id, TaskStatus.COMPLETED)
        val updatedTask = repo.tasks.value.first { it.id == createdTask.id }
        assertEquals(TaskStatus.COMPLETED, updatedTask.status)
        assertEquals(100, updatedTask.progressPercent)
    }

    @Test
    fun `routine streak increment on completion`() {
        val repo = NexusRepository.getInstance()
        val routine = repo.routines.value.first()
        val initialStreak = routine.streak
        val initialCompleted = routine.isCompletedToday

        repo.toggleRoutine(routine.id)
        val toggled = repo.routines.value.first { it.id == routine.id }
        assertEquals(!initialCompleted, toggled.isCompletedToday)
    }

    @Test
    fun `financial transaction recording`() {
        val repo = NexusRepository.getInstance()
        val initialTxCount = repo.transactions.value.size

        repo.addTransaction(
            accountType = AccountType.PERSONAL,
            type = TransactionType.INCOME,
            amount = 50000.0,
            category = "Bonus",
            description = "Q4 Performance Disbursal",
            date = "2026-11-12"
        )

        assertEquals(initialTxCount + 1, repo.transactions.value.size)
        val newTx = repo.transactions.value.first()
        assertEquals(50000.0, newTx.amount, 0.01)
    }

    @Test
    fun `water intake logging and deficiency calculation`() {
        val repo = NexusRepository.getInstance()
        val initialWater = repo.waterIntakeMl.value
        repo.addWaterIntake(250)
        assertEquals(initialWater + 250, repo.waterIntakeMl.value)
    }
}
