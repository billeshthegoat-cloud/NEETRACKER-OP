package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StudySessionEntity
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.PastelPillBadge
import com.example.ui.theme.BiologyGreen
import com.example.ui.theme.BlushPink
import com.example.ui.theme.CardGlassBorderLight
import com.example.ui.theme.ChemistryOrange
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.LavenderDark
import com.example.ui.theme.LavenderDeep
import com.example.ui.theme.LavenderPrimary
import com.example.ui.theme.LavenderSoftBg
import com.example.ui.theme.PhysicsBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.WarmPeach

@Composable
fun StudyTimerScreen(
    timerRemainingSeconds: Int,
    timerTotalSeconds: Int,
    isTimerRunning: Boolean,
    timerMode: String,
    timerSubject: String,
    sessions: List<StudySessionEntity>,
    onToggleTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onFinishSession: () -> Unit,
    onSetMode: (String) -> Unit,
    onSetSubject: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = timerRemainingSeconds / 60
    val seconds = timerRemainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val progress = if (timerTotalSeconds > 0) (timerRemainingSeconds.toFloat() / timerTotalSeconds.toFloat()) else 0f

    val modes = listOf("25/5", "50/10", "90m Mock")
    val subjects = listOf("Physics", "Chemistry", "Biology")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NEET Deep Focus Timer",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepBlack
                    )
                )
                Text(
                    text = "High-concentration pomodoro blocks to maximize retention",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight)
                )
            }
        }

        // Mode Selector Pills
        item {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .border(BorderStroke(1.2.dp, CardGlassBorderLight), RoundedCornerShape(24.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                modes.forEach { mode ->
                    val isSelected = timerMode == mode || (mode == "90m Mock" && timerMode == "Custom")
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) DeepBlack else Color.Transparent)
                            .clickable {
                                if (mode == "90m Mock") {
                                    onSetMode("Custom")
                                } else {
                                    onSetMode(mode)
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = mode,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextSecondaryLight
                            )
                        )
                    }
                }
            }
        }

        // Subject Selector Chips
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                subjects.forEach { subj ->
                    val isSelected = timerSubject == subj
                    val color = when (subj) {
                        "Physics" -> PhysicsBlue
                        "Chemistry" -> ChemistryOrange
                        else -> BiologyGreen
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) color.copy(alpha = 0.15f) else Color.White)
                            .border(
                                BorderStroke(
                                    1.dp,
                                    if (isSelected) color else CardGlassBorderLight
                                ),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onSetSubject(subj) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = subj,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) color else TextSecondaryLight
                            )
                        )
                    }
                }
            }
        }

        // Glowing Glass Circular Timer
        item {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                cornerRadius = 32.dp,
                elevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(240.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val animatedProgress by animateFloatAsState(
                            targetValue = progress,
                            animationSpec = tween(600, easing = FastOutSlowInEasing),
                            label = "timer_ring"
                        )

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 12.dp.toPx()
                            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                            // Track
                            drawArc(
                                color = LavenderSoftBg,
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )

                            // Progress
                            if (animatedProgress > 0f) {
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        colors = listOf(LavenderPrimary, LavenderDark, LavenderDeep, LavenderPrimary)
                                    ),
                                    startAngle = -90f,
                                    sweepAngle = 360f * animatedProgress,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = arcSize,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = timeFormatted,
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepBlack,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            PastelPillBadge(
                                text = if (isTimerRunning) "FOCUSING • $timerSubject" else "PAUSED",
                                bgColor = if (isTimerRunning) LavenderPrimary.copy(alpha = 0.3f) else WarmPeach,
                                textColor = DeepBlack
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Timer Control Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Reset Button
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(BorderStroke(1.2.dp, CardGlassBorderLight), CircleShape)
                                .clickable(onClick = onResetTimer)
                                .testTag("timer_reset_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset",
                                tint = TextSecondaryLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Play/Pause Main Button
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .shadow(8.dp, CircleShape, spotColor = DeepBlack.copy(alpha = 0.3f))
                                .clip(CircleShape)
                                .background(DeepBlack)
                                .clickable(onClick = onToggleTimer)
                                .testTag("timer_toggle_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Toggle",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        // Finish / Record Session Button
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen.copy(alpha = 0.15f))
                                .border(BorderStroke(1.2.dp, SuccessGreen), CircleShape)
                                .clickable(onClick = onFinishSession)
                                .testTag("timer_finish_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Finish",
                                tint = SuccessGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Recent Study Sessions History
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Recent Study Sessions",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepBlack
                    )
                )
                Text(
                    text = "Logged focus blocks contributing to daily goals",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                )
            }
        }

        items(sessions.take(5), key = { it.id }) { session ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 18.dp
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(LavenderSoftBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = null,
                                tint = LavenderDeep,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${session.subject} • ${session.chapterName}",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepBlack
                                )
                            )
                            Text(
                                text = "Mode: ${session.mode}",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight)
                            )
                        }
                    }
                    PastelPillBadge(
                        text = "+${session.durationMinutes} mins",
                        bgColor = LavenderPrimary.copy(alpha = 0.3f),
                        textColor = DeepBlack
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}
