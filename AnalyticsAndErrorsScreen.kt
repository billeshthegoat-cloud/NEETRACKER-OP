package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ErrorNoteEntity
import com.example.model.MockTestEntity
import com.example.model.UserProfileEntity
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientProgressBar
import com.example.ui.components.PastelPillBadge
import com.example.ui.theme.BiologyGreen
import com.example.ui.theme.BiologyGreenLight
import com.example.ui.theme.BlushPink
import com.example.ui.theme.CardGlassBorderLight
import com.example.ui.theme.ChemistryOrange
import com.example.ui.theme.ChemistryOrangeLight
import com.example.ui.theme.DeepBlack
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ErrorRedLight
import com.example.ui.theme.LavenderDark
import com.example.ui.theme.LavenderDeep
import com.example.ui.theme.LavenderPrimary
import com.example.ui.theme.LavenderSoftBg
import com.example.ui.theme.PhysicsBlue
import com.example.ui.theme.PhysicsBlueLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.WarmPeach
import com.example.ui.theme.WarningOrange

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnalyticsAndErrorsScreen(
    mockTests: List<MockTestEntity>,
    errorNotes: List<ErrorNoteEntity>,
    userProfile: UserProfileEntity?,
    onAddMockTest: (String, Int, Int, Int, Float, String, String) -> Unit,
    onDeleteMockTest: (MockTestEntity) -> Unit,
    onAddErrorNote: (String, String, String, String, String, String) -> Unit,
    onUpdateErrorStatus: (ErrorNoteEntity, String) -> Unit,
    onDeleteErrorNote: (ErrorNoteEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableStateOf(0) } // 0: Mock Analytics, 1: Error Notebook, 2: Badges
    var showAddMockDialog by remember { mutableStateOf(false) }
    var showAddErrorDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Segmented Control
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .border(BorderStroke(1.2.dp, CardGlassBorderLight), RoundedCornerShape(20.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val tabs = listOf("Mock Analytics", "Error Notebook (${errorNotes.size})", "Badges & XP")
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedSection == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) DeepBlack else Color.Transparent)
                            .clickable { selectedSection = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextSecondaryLight
                            )
                        )
                    }
                }
            }
        }

        when (selectedSection) {
            0 -> {
                // 1. MOCK TEST ANALYTICS & CHARTS
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Mock Test Trajectory",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Score trends, subject breakdown & percentile radar",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                            )
                        }
                        GlassButton(
                            text = "+ Test",
                            onClick = { showAddMockDialog = true },
                            isPrimary = true,
                            testTag = "add_mock_test_btn"
                        )
                    }
                }

                // Interactive Trajectory Line Graph
                item {
                    MockScoreTrajectoryCard(mockTests = mockTests)
                }

                // Latest Mock Test Detailed Breakdown
                val latest = mockTests.firstOrNull()
                if (latest != null) {
                    item {
                        GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 24.dp) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = latest.testName,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = DeepBlack
                                            )
                                        )
                                        Text(
                                            text = "Date: ${latest.dateString} • Percentile: ${latest.percentile}%",
                                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                                        )
                                    }
                                    PastelPillBadge(
                                        text = "${latest.totalScore} / 720",
                                        bgColor = LavenderSoftBg,
                                        textColor = DeepBlack
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Subject Score Bars
                                SubjectScoreRow("Physics", latest.physicsScore, 180, PhysicsBlue)
                                Spacer(modifier = Modifier.height(8.dp))
                                SubjectScoreRow("Chemistry", latest.chemistryScore, 180, ChemistryOrange)
                                Spacer(modifier = Modifier.height(8.dp))
                                SubjectScoreRow("Biology", latest.biologyScore, 360, BiologyGreen)

                                if (latest.notes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Analysis: ${latest.notes}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextPrimaryLight)
                                    )
                                }
                            }
                        }
                    }
                }

                // Past Mock Tests History List
                item {
                    Text(
                        text = "All Recorded Tests",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                items(mockTests, key = { it.id }) { test ->
                    GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 18.dp) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = test.testName,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Phy: ${test.physicsScore} | Chem: ${test.chemistryScore} | Bio: ${test.biologyScore} • Acc: ${test.accuracyPercent}%",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                PastelPillBadge(
                                    text = "${test.totalScore}/720",
                                    bgColor = LavenderPrimary.copy(alpha = 0.3f),
                                    textColor = DeepBlack
                                )
                                IconButton(onClick = { onDeleteMockTest(test) }, modifier = Modifier.size(28.dp)) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = TextMutedLight)
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // 2. ERROR NOTEBOOK
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "NEET Error Notebook",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Never repeat a mistake: review traps & NCERT lines",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                            )
                        }
                        GlassButton(
                            text = "+ Log Error",
                            onClick = { showAddErrorDialog = true },
                            isPrimary = true,
                            testTag = "add_error_note_btn"
                        )
                    }
                }

                items(errorNotes, key = { it.id }) { note ->
                    ErrorNoteCardItem(
                        note = note,
                        onUpdateStatus = { newStat -> onUpdateErrorStatus(note, newStat) },
                        onDelete = { onDeleteErrorNote(note) }
                    )
                }
            }

            2 -> {
                // 3. BADGES & GAMIFICATION
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 26.dp) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(WarmPeach),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.EmojiEvents,
                                            contentDescription = null,
                                            tint = DeepBlack,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Level ${userProfile?.level ?: 15} Aspirant",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = DeepBlack
                                            )
                                        )
                                        Text(
                                            text = "${userProfile?.totalXp ?: 5420} XP Total Accumulated",
                                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                                        )
                                    }
                                }
                                PastelPillBadge(
                                    text = "🔥 ${userProfile?.currentStreak ?: 185} Days",
                                    bgColor = WarmPeach,
                                    textColor = DeepBlack
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            val nextLevelXp = ((userProfile?.level ?: 15)) * 400
                            val currLevelXp = (userProfile?.totalXp ?: 5420) % 400
                            val xpProgress = (currLevelXp.toFloat() / 400f).coerceIn(0f, 1f)

                            Text(
                                text = "Next Level in ${400 - currLevelXp} XP",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = LavenderDeep
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            GradientProgressBar(
                                progress = xpProgress,
                                height = 10.dp,
                                gradientColors = listOf(LavenderPrimary, LavenderDeep)
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Achievements & Trophies",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepBlack
                        )
                    )
                }

                val allBadges = listOf(
                    BadgeInfo("100-Day Streak", "Maintained 100 consecutive days of study", "STREAK_100", true, Icons.Default.LocalFireDepartment, WarningOrange),
                    BadgeInfo("Biology Champion", "Scored 340+ in Biology mock test", "BIO_CHAMP", true, Icons.Default.MilitaryTech, BiologyGreen),
                    BadgeInfo("Night Owl", "Completed 50+ hours in late evening focus blocks", "NIGHT_OWL", true, Icons.Default.Star, LavenderDeep),
                    BadgeInfo("Question Crusher", "Solved 3,000+ NEET PYQs & MCQs", "QUESTION_CRUSHER", true, Icons.Default.Insights, PhysicsBlue),
                    BadgeInfo("Mock Warrior", "Gave 10+ full syllabus tests with >98%ile", "MOCK_WARRIOR", true, Icons.Default.EmojiEvents, WarmPeach),
                    BadgeInfo("700+ Score Club", "Cross 700 marks in full length AIIMS mock", "SCORE_700", false, Icons.Default.Lock, TextMutedLight),
                    BadgeInfo("Syllabus Master", "100% completion across all 97 chapters", "SYLLABUS_100", false, Icons.Default.Lock, TextMutedLight)
                )

                items(allBadges) { badge ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 20.dp,
                        backgroundColor = if (badge.isUnlocked) Color.White.copy(alpha = 0.9f) else Color(0xFFF6F5FA)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(if (badge.isUnlocked) badge.iconColor.copy(alpha = 0.15f) else Color(0xFFEBE9F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = badge.icon,
                                    contentDescription = null,
                                    tint = if (badge.isUnlocked) badge.iconColor else TextMutedLight,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = badge.name,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (badge.isUnlocked) DeepBlack else TextMutedLight
                                    )
                                )
                                Text(
                                    text = badge.description,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (badge.isUnlocked) TextSecondaryLight else TextMutedLight
                                    )
                                )
                            }
                            PastelPillBadge(
                                text = if (badge.isUnlocked) "UNLOCKED" else "LOCKED",
                                bgColor = if (badge.isUnlocked) SuccessGreenLight else Color(0xFFEBE9F0),
                                textColor = if (badge.isUnlocked) SuccessGreen else TextMutedLight
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    if (showAddMockDialog) {
        AddMockTestDialog(
            onDismiss = { showAddMockDialog = false },
            onConfirm = { name, phy, chem, bio, acc, date, notes ->
                onAddMockTest(name, phy, chem, bio, acc, date, notes)
                showAddMockDialog = false
            }
        )
    }

    if (showAddErrorDialog) {
        AddErrorNoteDialog(
            onDismiss = { showAddErrorDialog = false },
            onConfirm = { subj, chap, sum, mis, con, tag ->
                onAddErrorNote(subj, chap, sum, mis, con, tag)
                showAddErrorDialog = false
            }
        )
    }
}

data class BadgeInfo(
    val name: String,
    val description: String,
    val key: String,
    val isUnlocked: Boolean,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconColor: Color
)

@Composable
private fun SubjectScoreRow(title: String, score: Int, max: Int, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
            Text("$score / $max", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = color))
        }
        Spacer(modifier = Modifier.height(4.dp))
        GradientProgressBar(
            progress = score.toFloat() / max.toFloat(),
            height = 6.dp,
            gradientColors = listOf(color, color.copy(alpha = 0.8f))
        )
    }
}

@Composable
private fun MockScoreTrajectoryCard(mockTests: List<MockTestEntity>) {
    GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 24.dp) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Score Growth Trajectory",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                PastelPillBadge(
                    text = "+73 Marks Gain 🔥",
                    bgColor = SuccessGreenLight,
                    textColor = SuccessGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Canvas Line Graph
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val scores = if (mockTests.isNotEmpty()) mockTests.reversed().map { it.totalScore } else listOf(595, 625, 645, 668)
                    val minScore = 550f
                    val maxScore = 720f

                    val points = scores.mapIndexed { index, score ->
                        val x = (size.width / maxOf(1, scores.size - 1)) * index
                        val y = size.height - ((score - minScore) / (maxScore - minScore)) * size.height
                        Offset(x, y.coerceIn(10f, size.height - 10f))
                    }

                    // Draw line
                    val path = Path()
                    points.forEachIndexed { i, pt ->
                        if (i == 0) path.moveTo(pt.x, pt.y) else path.lineTo(pt.x, pt.y)
                    }

                    drawPath(
                        path = path,
                        brush = Brush.horizontalGradient(listOf(LavenderPrimary, LavenderDeep)),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Draw dots
                    points.forEach { pt ->
                        drawCircle(color = Color.White, radius = 6.dp.toPx(), center = pt)
                        drawCircle(color = DeepBlack, radius = 3.5.dp.toPx(), center = pt)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Test 1 (595)", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                Text("Test 2 (625)", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                Text("Test 3 (645)", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                Text("Major 4 (668)", style = MaterialTheme.typography.labelSmall.copy(color = LavenderDeep, fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
private fun ErrorNoteCardItem(
    note: ErrorNoteEntity,
    onUpdateStatus: (String) -> Unit,
    onDelete: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 22.dp,
        borderColor = when (note.status) {
            "MASTERED" -> SuccessGreen.copy(alpha = 0.4f)
            "REVIEWING" -> WarningOrange.copy(alpha = 0.4f)
            else -> ErrorRed.copy(alpha = 0.4f)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${note.subject} • ${note.chapterName}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = LavenderDeep
                        )
                    )
                    Text(
                        text = note.questionSummary,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepBlack
                        )
                    )
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = TextMutedLight)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mistake vs Correct concept comparison box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFAF9FD))
                    .border(BorderStroke(1.dp, CardGlassBorderLight), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "❌ My Mistake: ${note.mistakeDescription}",
                    style = MaterialTheme.typography.bodySmall.copy(color = ErrorRed, fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "💡 NCERT Fact / Rule: ${note.correctConcept}",
                    style = MaterialTheme.typography.bodySmall.copy(color = SuccessGreen, fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PastelPillBadge(
                    text = note.tag,
                    bgColor = LavenderSoftBg,
                    textColor = DeepBlack
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val statuses = listOf("UNSOLVED", "REVIEWING", "MASTERED")
                    statuses.forEach { st ->
                        val isSelected = note.status == st
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) {
                                        when (st) {
                                            "MASTERED" -> SuccessGreenLight
                                            "REVIEWING" -> WarmPeach
                                            else -> ErrorRedLight
                                        }
                                    } else Color.Transparent
                                )
                                .clickable { onUpdateStatus(st) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = st,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = if (isSelected) DeepBlack else TextMutedLight
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddMockTestDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Int, Int, Float, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phy by remember { mutableStateOf("150") }
    var chem by remember { mutableStateOf("160") }
    var bio by remember { mutableStateOf("340") }
    var acc by remember { mutableStateOf("90.0") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Mock Test Result", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Test Name / Series") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(value = phy, onValueChange = { phy = it }, label = { Text("Phy (/180)") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = chem, onValueChange = { chem = it }, label = { Text("Chem (/180)") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Bio (/360)") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = acc, onValueChange = { acc = it }, label = { Text("Accuracy %") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Key Takeaways & Traps") })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        val p = phy.toIntOrNull() ?: 0
                        val c = chem.toIntOrNull() ?: 0
                        val b = bio.toIntOrNull() ?: 0
                        val a = acc.toFloatOrNull() ?: 85f
                        onConfirm(name, p, c, b, a, "Today", notes)
                    }
                }
            ) {
                Text("Save Test", fontWeight = FontWeight.Bold, color = LavenderDeep)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun AddErrorNoteDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String) -> Unit
) {
    var subject by remember { mutableStateOf("Physics") }
    var chapter by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var mistake by remember { mutableStateOf("") }
    var concept by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("Concept Trap") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Mistake to Error Notebook", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject") }, singleLine = true)
                OutlinedTextField(value = chapter, onValueChange = { chapter = it }, label = { Text("Chapter Name") }, singleLine = true)
                OutlinedTextField(value = summary, onValueChange = { summary = it }, label = { Text("Question / Concept Summary") }, singleLine = true)
                OutlinedTextField(value = mistake, onValueChange = { mistake = it }, label = { Text("What mistake did you make?") })
                OutlinedTextField(value = concept, onValueChange = { concept = it }, label = { Text("Correct NCERT Concept / Formula") })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (chapter.isNotBlank() && mistake.isNotBlank() && concept.isNotBlank()) {
                        onConfirm(subject, chapter, summary, mistake, concept, tag)
                    }
                }
            ) {
                Text("Save Error", fontWeight = FontWeight.Bold, color = LavenderDeep)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
