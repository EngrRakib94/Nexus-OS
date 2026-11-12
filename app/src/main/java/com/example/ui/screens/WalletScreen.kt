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
import com.example.data.model.AccountType
import com.example.data.model.FinancialTransaction
import com.example.data.model.TransactionType
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun WalletScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.transactions.collectAsState()
    val budgets by viewModel.budgets.collectAsState()
    val currency by viewModel.currency.collectAsState()

    var selectedAccountType by remember { mutableStateOf(AccountType.PERSONAL) }

    val accountTransactions = transactions.filter { it.accountType == selectedAccountType }
    val totalIncome = accountTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val totalExpense = accountTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val balance = totalIncome - totalExpense

    val accountBudgets = budgets.filter { it.accountType == selectedAccountType }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Account Type Selector: Personal vs Business
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AccountType.values().forEach { acc ->
                val isSel = selectedAccountType == acc
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSel) NeonGreen.copy(alpha = 0.2f) else CyberSurface)
                        .border(1.dp, if (isSel) NeonGreen else CyberBorder, RoundedCornerShape(4.dp))
                        .clickable { selectedAccountType = acc }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${acc.label.uppercase()} LEDGER",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) NeonGreen else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Balance Summary HUD Card
        CyberCard(
            borderColor = NeonGreen,
            backgroundColor = CyberBackgroundSecondary
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AVAILABLE RESERVE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted,
                            fontSize = 9.sp
                        )
                    )
                    Text(
                        text = "${balance.toInt()} $currency",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen,
                            fontSize = 24.sp
                        )
                    )
                }

                CyberButton(
                    text = "+ RECORD",
                    onClick = { viewModel.showCreateTransactionDialog.value = true }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Income card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyberSurface)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("TOTAL INFLOW", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
                        Text("+${totalIncome.toInt()} $currency", fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = NeonGreen)
                    }
                }

                // Expense card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyberSurface)
                        .padding(8.dp)
                ) {
                    Column {
                        Text("TOTAL OUTFLOW", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = TextMuted)
                        Text("-${totalExpense.toInt()} $currency", fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = NeonRed)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Budget & Allocation Progress
        if (accountBudgets.isNotEmpty()) {
            CyberCard(
                borderColor = CyberBorder,
                backgroundColor = CyberSurface
            ) {
                Text(
                    text = "CATEGORY BUDGET ALLOCATIONS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))

                accountBudgets.forEach { budget ->
                    val fraction = (budget.currentSpent / budget.monthlyLimit).toFloat().coerceIn(0f, 1f)
                    val isExceeded = budget.currentSpent > budget.monthlyLimit

                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(budget.category, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = TextSecondary)
                            Text(
                                "${budget.currentSpent.toInt()} / ${budget.monthlyLimit.toInt()} $currency",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = if (isExceeded) NeonRed else NeonCyan
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        LinearProgressIndicator(
                            progress = { fraction },
                            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                            color = if (isExceeded) NeonRed else NeonCyan,
                            trackColor = CyberBorder
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        CyberSectionHeader(
            title = "Ledger Audit Trail",
            subtitle = "Sequential transactions"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(accountTransactions, key = { it.id }) { tx ->
                val isIncome = tx.type == TransactionType.INCOME
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
                        Column {
                            Text(
                                text = tx.description,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = TextPrimary
                            )
                            Text(
                                text = "${tx.category} // ${tx.date}",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = TextMuted
                            )
                        }

                        Text(
                            text = "${if (isIncome) "+" else "-"}${tx.amount.toInt()} $currency",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = if (isIncome) NeonGreen else NeonRed
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
