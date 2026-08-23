package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.api.GeminiClient
import com.example.model.UserProfileEntity
import com.example.ui.theme.CardGlassBorderLight
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.LavenderDark
import com.example.ui.theme.LavenderDeep
import com.example.ui.theme.LavenderPrimary
import com.example.ui.theme.LavenderSoftBg
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.WarmPeach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AiCoachBottomSheet(
    onDismiss: () -> Unit,
    userProfile: UserProfileEntity?,
    onUpdateProfile: (Int, String, String) -> Unit,
    initialChapterTipsPrompt: Pair<String, String>? = null
) {
    val coroutineScope = rememberCoroutineScope()
    var userQuery by remember {
        mutableStateOf(
            if (initialChapterTipsPrompt != null) {
                "Give me 3 high-yield NEET tips and mistake traps for ${initialChapterTipsPrompt.first} (${initialChapterTipsPrompt.second})"
            } else ""
        )
    }
    var aiResponse by remember {
        mutableStateOf(
            if (initialChapterTipsPrompt != null) "Generating high-yield insights for ${initialChapterTipsPrompt.first}..."
            else "Namaste Dr. Aspirant! I'm your NEET Strategic Coach. Ask me about NCERT revision hacks, formula derivations, high-yield traps, or 680+ score blueprints."
        )
    }
    var isLoading by remember { mutableStateOf(initialChapterTipsPrompt != null) }

    // If initialized with chapter tips, auto-fetch
    remember {
        if (initialChapterTipsPrompt != null) {
            coroutineScope.launch {
                val tips = GeminiClient.generateChapterQuizOrTips(initialChapterTipsPrompt.first, initialChapterTipsPrompt.second)
                aiResponse = tips
                isLoading = false
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(LavenderPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = DeepBlack,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "NEETRACKER AI Coach",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepBlack
                            )
                        )
                        Text(
                            text = "Target: ${userProfile?.targetCollege ?: "AIIMS Delhi"}",
                            style = MaterialTheme.typography.labelSmall.copy(color = LavenderDeep)
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            // Quick Prompt Chips
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val suggestions = listOf(
                    "Botany NCERT line-by-line strategy",
                    "Physics Ray Optics formula tricks",
                    "How to boost score from 620 to 680+",
                    "Organic Chemistry reaction roadmap"
                )
                suggestions.forEach { suggestion ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(LavenderSoftBg)
                            .border(BorderStroke(1.dp, CardGlassBorderLight), RoundedCornerShape(12.dp))
                            .clickable {
                                userQuery = suggestion
                                coroutineScope.launch {
                                    isLoading = true
                                    aiResponse = GeminiClient.generateChapterQuizOrTips(suggestion, "General NEET Strategy")
                                    isLoading = false
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = suggestion,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = DeepBlack
                            )
                        )
                    }
                }
            }

            // AI Response Glass Container
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                backgroundColor = Color(0xFFFAF8FF)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = LavenderDeep,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Coach Recommendations",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = LavenderDeep
                                )
                            )
                        }

                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = LavenderDeep
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = aiResponse,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextPrimaryLight,
                            lineHeight = 22.sp
                        )
                    )
                }
            }

            // Chat Input Box
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = userQuery,
                    onValueChange = { userQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .testTag("ai_coach_query_input"),
                    placeholder = {
                        Text(
                            "Ask AI Coach anything...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextMutedLight)
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LavenderPrimary,
                        unfocusedBorderColor = CardGlassBorderLight
                    )
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(DeepBlack)
                        .clickable {
                            if (userQuery.isNotBlank()) {
                                val q = userQuery
                                coroutineScope.launch {
                                    isLoading = true
                                    aiResponse = GeminiClient.generateChapterQuizOrTips(q, "NEET Aspirant Query")
                                    isLoading = false
                                }
                            }
                        }
                        .testTag("ai_coach_send_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ProfileDialog(
    userProfile: UserProfileEntity?,
    onDismiss: () -> Unit,
    onSave: (Int, String, String) -> Unit
) {
    var name by remember { mutableStateOf(userProfile?.name ?: "Aryan Sharma") }
    var targetCollege by remember { mutableStateOf(userProfile?.targetCollege ?: "AIIMS New Delhi") }
    var targetScore by remember { mutableStateOf((userProfile?.targetScore ?: 690).toString()) }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("NEET Aspirant Profile", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Aspirant Name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = targetCollege,
                    onValueChange = { targetCollege = it },
                    label = { Text("Target Medical College") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = targetScore,
                    onValueChange = { targetScore = it },
                    label = { Text("Target NEET Score (/720)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = {
                    val score = targetScore.toIntOrNull() ?: 690
                    onSave(score, targetCollege, name)
                    onDismiss()
                }
            ) {
                Text("Save Profile", fontWeight = FontWeight.Bold, color = LavenderDeep)
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
