package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ChapterEntity
import com.example.model.HabitEntity
import com.example.model.LectureEntity
import com.example.model.SubjectType
import com.example.model.TaskEntity
import com.example.model.UserProfileEntity
import com.example.ui.components.CircularProgressRing
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientProgressBar
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
import com.example.ui.theme.SuccessGreenLight
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.WarmPeach
import com.example.ui.theme.WarningOrange

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    userProfile: UserProfileEntity?,
    chapters: List<ChapterEntity>,
    lectures: List<LectureEntity>,
    tasks: List<TaskEntity>,
    habits: List<HabitEntity>,
    aiInsight: String,
    isAiLoading: Boolean,
    onRefreshAi: () -> Unit,
    onNavigateToSyllabus: () -> Unit,
    onNavigateToTimer: () -> Unit,
    onNavigateToLectures: () -> Unit,
    onToggleTask: (TaskEntity) -> Unit,
    onToggleHabit: (HabitEntity) -> Unit,
    onOpenAiCoachDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalChapters = if (chapters.isNotEmpty()) chapters.size else 1
    val completedChapters = chapters.count { it.isCompleted }
    val overallSyllabusPercent = (completedChapters * 100) / totalChapters

    val physicsChapters = chapters.filter { it.subject == SubjectType.PHYSICS.name }
    val physicsCompleted = physicsChapters.count { it.isCompleted }
    val physicsQuestions = physicsChapters.sumOf { it.questionsSolved }
    val physicsLecs = physicsChapters.sumOf { it.completedLectures }
    val physicsPercent = if (physicsChapters.isNotEmpty()) (physicsCompleted * 100) / physicsChapters.size else 42

    val chemChapters = chapters.filter { it.subject.startsWith("CHEMISTRY") }
    val chemCompleted = chemChapters.count { it.isCompleted }
    val chemQuestions = chemChapters.sumOf { it.questionsSolved }
    val chemLecs = chemChapters.sumOf { it.completedLectures }
    val chemPercent = if (chemChapters.isNotEmpty()) (chemCompleted * 100) / chemChapters.size else 67

    val bioChapters = chapters.filter { it.subject.startsWith("BIOLOGY") }
    val bioCompleted = bioChapters.count { it.isCompleted }
    val bioQuestions = bioChapters.sumOf { it.questionsSolved }
    val bioLecs = bioChapters.sumOf { it.completedLectures }
    val bioPercent = if (bioChapters.isNotEmpty()) (bioCompleted * 100) / bioChapters.size else 84

    val pendingTasks = tasks.filter { !it.isCompleted }
    val todayCompletedTasks = tasks.count { it.isCompleted }
    val totalTasksCount = if (tasks.isNotEmpty()) tasks.size else 1
    val dailyTaskProgress = (todayCompletedTasks.toFloat() / totalTasksCount).coerceIn(0f, 1f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Clean Minimalism Signature Hero Section Card
        item {
            CleanMinimalismHeroCard(
                userName = userProfile?.name ?: "Ishan",
                streakDays = userProfile?.currentStreak ?: 185,
                dailyCompletionPercent = (dailyTaskProgress * 100).toInt().coerceAtLeast(78),
                hoursStudied = userProfile?.totalHoursStudied ?: 7.4f,
                bioQuestions = physicsQuestions.coerceAtLeast(12),
                physicsLecs = physicsLecs.coerceAtLeast(4)
            )
        }

        // 2. Subjects Progress Horizontal Strip (Clean Minimalism style)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subjects Progress",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepBlack
                    )
                )
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = LavenderDark,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onNavigateToSyllabus() }
                )
            }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SubjectCleanCard(
                        title = "Biology",
                        masteryPercent = bioPercent,
                        accentColor = LavenderDark,
                        bgTint = LavenderPrimary.copy(alpha = 0.20f),
                        icon = Icons.Default.Biotech,
                        onClick = onNavigateToSyllabus
                    )
                }
                item {
                    SubjectCleanCard(
                        title = "Physics",
                        masteryPercent = physicsPercent,
                        accentColor = WarningOrange,
                        bgTint = BlushPink.copy(alpha = 0.25f),
                        icon = Icons.Default.ElectricBolt,
                        onClick = onNavigateToSyllabus
                    )
                }
                item {
                    SubjectCleanCard(
                        title = "Chemistry",
                        masteryPercent = chemPercent,
                        accentColor = BlushPink,
                        bgTint = WarmPeach.copy(alpha = 0.35f),
                        icon = Icons.Default.Science,
                        onClick = onNavigateToSyllabus
                    )
                }
            }
        }

        // 3. Clean Focus Timer Action Banner (High contrast black card)
        item {
            FocusTimerBanner(
                onStart = onNavigateToTimer
            )
        }

        // 4. AI Performance Coach Insights Card
        item {
            AiPerformanceInsightCard(
                insightText = aiInsight,
                isLoading = isAiLoading,
                onRefresh = onRefreshAi,
                onOpenChat = onOpenAiCoachDialog
            )
        }

        // 5. Daily Habit Consistency Matrix
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Habit Streaks",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepBlack
                    )
                )
                Text(
                    text = "View All",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = LavenderDark,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onNavigateToLectures() }
                )
            }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(habits) { habit ->
                    HabitMiniPill(
                        habit = habit,
                        onToggle = { onToggleHabit(habit) }
                    )
                }
            }
        }

        // 6. Today's Checklist
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 28.dp
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's Tasks (${pendingTasks.size} remaining)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepBlack
                            )
                        )
                        PastelPillBadge(
                            text = "${todayCompletedTasks}/${totalTasksCount} Done",
                            bgColor = SuccessGreenLight,
                            textColor = SuccessGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    tasks.take(4).forEach { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { onToggleTask(task) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                                contentDescription = null,
                                tint = if (task.isCompleted) SuccessGreen else LavenderDark,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (task.isCompleted) TextMutedLight else TextPrimaryLight,
                                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium
                                    )
                                )
                            }
                            PastelPillBadge(
                                text = task.priority,
                                bgColor = when (task.priority) {
                                    "HIGH" -> BlushPink
                                    "MEDIUM" -> WarmPeach
                                    else -> LavenderSoftBg
                                },
                                textColor = DeepBlack
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp)) // Safe padding for bottom dock
        }
    }
}

@Composable
private fun CleanMinimalismHeroCard(
    userName: String,
    streakDays: Int,
    dailyCompletionPercent: Int,
    hoursStudied: Float,
    bioQuestions: Int,
    physicsLecs: Int
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 32.dp,
        backgroundColor = Color.White.copy(alpha = 0.50f),
        borderColor = Color.White.copy(alpha = 0.70f),
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            // Header row with Name and Fire streak
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Keep going, $userName!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepBlack
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "You've completed $dailyCompletionPercent% of today's goal.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondaryLight
                        )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "🔥",
                        fontSize = 24.sp
                    )
                    Text(
                        text = "$streakDays Days",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepBlack
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main stats container: Left Circular Ring (7.4 Hours) + Right 2 Cards Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Circular Progress Ring for Study Hours
                CircularProgressRing(
                    progress = (hoursStudied / 10f).coerceIn(0f, 1f),
                    size = 92.dp,
                    strokeWidth = 8.dp,
                    gradientColors = listOf(LavenderPrimary, LavenderDark),
                    trackColor = LavenderSoftBg
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%.1f", hoursStudied),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepBlack
                            )
                        )
                        Text(
                            text = "HOURS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = TextSecondaryLight
                            )
                        )
                    }
                }

                // Grid of 2 mini stats cards
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MiniHeroStatCard(
                        subject = "BIOLOGY",
                        value = "$bioQuestions/15 Qs"
                    )
                    MiniHeroStatCard(
                        subject = "PHYSICS",
                        value = "$physicsLecs/6 Lcts"
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniHeroStatCard(
    subject: String,
    value: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.55f))
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column {
            Text(
                text = subject,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = TextSecondaryLight,
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepBlack
                )
            )
        }
    }
}

@Composable
private fun SubjectCleanCard(
    title: String,
    masteryPercent: Int,
    accentColor: Color,
    bgTint: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(145.dp)
            .shadow(4.dp, RoundedCornerShape(28.dp), spotColor = accentColor.copy(alpha = 0.2f))
            .clip(RoundedCornerShape(28.dp))
            .background(bgTint)
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)), RoundedCornerShape(28.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // White circular icon badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .shadow(2.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepBlack
                    )
                )
                Text(
                    text = "$masteryPercent% Mastery",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondaryLight,
                        fontSize = 11.sp
                    )
                )
            }

            // Minimal Progress Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.White.copy(alpha = 0.6f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(masteryPercent / 100f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(accentColor)
                )
            }
        }
    }
}

@Composable
private fun FocusTimerBanner(
    onStart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(28.dp), spotColor = DeepBlack.copy(alpha = 0.25f))
            .clip(RoundedCornerShape(28.dp))
            .background(DeepBlack)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⌛",
                        fontSize = 20.sp
                    )
                }
                Column {
                    Text(
                        text = "Focus Timer",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "50:00 Deep Work Session",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White.copy(alpha = 0.65f)
                        )
                    )
                }
            }

            // White Clean Start Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .clickable { onStart() }
                    .padding(horizontal = 18.dp, vertical = 8.dp)
                    .testTag("dashboard_start_timer_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Start",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepBlack
                    )
                )
            }
        }
    }
}

@Composable
private fun AiPerformanceInsightCard(
    insightText: String,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onOpenChat: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 28.dp,
        borderColor = Color.White.copy(alpha = 0.7f),
        backgroundColor = Color(0xFFFAF8FF)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(LavenderPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = DeepBlack,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "AI Performance Insights",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepBlack
                        )
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = LavenderDark
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(BorderStroke(1.dp, CardGlassBorderLight), RoundedCornerShape(12.dp))
                            .clickable(onClick = onRefresh)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Refresh",
                            tint = LavenderDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Refresh",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = LavenderDark
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = insightText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimaryLight,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.9f))
                    .border(BorderStroke(1.dp, CardGlassBorderLight), RoundedCornerShape(16.dp))
                    .clickable(onClick = onOpenChat)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ask AI Coach for Custom Chapter Strategy →",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = LavenderDark
                    )
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = LavenderDark,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun HabitMiniPill(
    habit: HabitEntity,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (habit.isTodayCompleted) LavenderPrimary.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.7f))
            .border(
                BorderStroke(
                    1.dp,
                    if (habit.isTodayCompleted) LavenderDark else CardGlassBorderLight
                ),
                RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onToggle)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (habit.isTodayCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
            contentDescription = null,
            tint = if (habit.isTodayCompleted) LavenderDark else TextMutedLight,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = habit.name,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = DeepBlack
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "${habit.currentStreak}d",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = WarningOrange
            )
        )
    }
}
