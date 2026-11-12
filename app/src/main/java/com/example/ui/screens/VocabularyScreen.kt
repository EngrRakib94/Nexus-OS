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
import com.example.data.model.VocabularyWord
import com.example.ui.NexusViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun VocabularyScreen(
    viewModel: NexusViewModel,
    modifier: Modifier = Modifier
) {
    val vocabulary by viewModel.vocabularyWords.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var filterTab by remember { mutableStateOf("ALL") } // ALL, FAVORITE, UNLEARNED
    var showAddWordDialog by remember { mutableStateOf(false) }

    val filteredWords = vocabulary.filter { word ->
        val matchesQuery = searchQuery.isBlank() ||
                word.englishWord.contains(searchQuery, ignoreCase = true) ||
                word.bengaliMeaning.contains(searchQuery, ignoreCase = true)

        val matchesTab = when (filterTab) {
            "FAVORITE" -> word.isFavorite
            "UNLEARNED" -> !word.isLearned
            else -> true
        }

        matchesQuery && matchesTab
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Search & Add word row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search word or Bengali...", fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            CyberButton(
                text = "+ WORD",
                onClick = { showAddWordDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter tabs: ALL, FAVORITES, UNLEARNED
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL" to "ALL (${vocabulary.size})", "FAVORITE" to "STARRED", "UNLEARNED" to "UNLEARNED").forEach { (tabKey, label) ->
                val isSel = filterTab == tabKey
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSel) NeonCyan.copy(alpha = 0.2f) else CyberSurface)
                        .border(1.dp, if (isSel) NeonCyan else CyberBorder, RoundedCornerShape(4.dp))
                        .clickable { filterTab = tabKey }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSel) NeonCyan else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredWords, key = { it.id }) { word ->
                CyberCard(
                    borderColor = if (word.isLearned) NeonGreen.copy(alpha = 0.4f) else CyberBorder,
                    backgroundColor = CyberSurface
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = word.englishWord,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan
                                )
                            )
                            if (word.pronunciation.isNotBlank()) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = word.pronunciation,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.toggleWordFavorite(word.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (word.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = null,
                                    tint = if (word.isFavorite) NeonAmber else TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            CyberBadge(
                                text = if (word.isLearned) "LEARNED" else "PENDING",
                                color = if (word.isLearned) NeonGreen else NeonAmber
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "বাংলা অর্থ: ${word.bengaliMeaning}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${word.exampleSentence}\"",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { viewModel.toggleWordLearned(word.id) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (word.isLearned) "MARK UNLEARNED" else "MARK AS LEARNED",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = if (word.isLearned) TextMuted else NeonGreen
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showAddWordDialog) {
        var english by remember { mutableStateOf("") }
        var pronunciation by remember { mutableStateOf("") }
        var bengali by remember { mutableStateOf("") }
        var example by remember { mutableStateOf("") }

        androidx.compose.ui.window.Dialog(onDismissRequest = { showAddWordDialog = false }) {
            CyberCard(
                borderColor = NeonCyan,
                backgroundColor = CyberBackgroundSecondary
            ) {
                Text(
                    text = "REGISTER NEW VOCABULARY",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = english, onValueChange = { english = it }, label = { Text("English Word") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = pronunciation, onValueChange = { pronunciation = it }, label = { Text("Phonetic Pronunciation") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = bengali, onValueChange = { bengali = it }, label = { Text("বাংলা অর্থ (Bengali Meaning)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = example, onValueChange = { example = it }, label = { Text("Example Sentence") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                CyberButton(
                    text = "ADD TO VOCABULARY ENGINE",
                    onClick = {
                        if (english.isNotBlank() && bengali.isNotBlank()) {
                            viewModel.addWord(english, bengali, example, pronunciation)
                            showAddWordDialog = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
