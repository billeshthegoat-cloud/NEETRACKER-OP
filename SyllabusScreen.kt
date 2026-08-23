package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChapterEntity
import com.example.model.SubjectType
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientProgressBar
import com.example.ui.components.PastelPillBadge
import com.example.ui.theme.BiologyGreen
import com.example.ui.theme.BiologyGreenLight
import com.example.ui.theme.BlushPink
import com.example.ui.theme.BotanyTeal
import com.example.ui.theme.CardGlassBorderLight
import com.example.ui.theme.ChemistryOrange
import com.example.ui.theme.ChemistryOrangeLight
import com.example.ui.theme.DeepBlack
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
import com.example.ui.theme.ZoologyRose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyllabusScreen(
    chapters: List<ChapterEntity>,
    searchQuery: String,
    selectedSubjectFilter: String,
    onSearchChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onToggleChapter: (ChapterEntity) -> Unit,
    onUpdateChapter: (ChapterEntity) -> Unit,
    onAskAiForChapterTips: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var activeChapterForDetail by remember { mutableStateOf<ChapterEntity?>(null) }
    var selectedStatusFilter by remember { mutableStateOf("ALL") }

    val filterTabs = listOf(
        "ALL" to "All Chapters",
        SubjectType.PHYSICS.name to "Physics (28)",
        SubjectType.CHEMISTRY_PHYSICAL.name to "Physical Chem (7)",
        SubjectType.CHEMISTRY_ORGANIC.name to "Organic Chem (8)",
        SubjectType.CHEMISTRY_INORGANIC.name to "Inorganic Chem (6)",
        SubjectType.BIOLOGY_BOTANY.name to "Botany (14)",
        SubjectType.BIOLOGY_ZOOLOGY.name to "Zoology (14)"
    )

    val filteredChapters = chapters.filter { chapter ->
        val matchesSubject = selectedSubjectFilter == "ALL" || chapter.subject == selectedSubjectFilter
        val matchesSearch = searchQuery.isBlank() || chapter.name.contains(searchQuery, ignoreCase = true)
        val matchesStatus = when (selectedStatusFilter) {
            "COMPLETED" -> chapter.isCompleted
            "IN_PROGRESS" -> !chapter.isCompleted && (chapter.completedLectures > 0 || chapter.questionsSolved > 0)
            "NOT_STARTED" -> !chapter.isCompleted && chapter.completedLectures == 0 && chapter.questionsSolved == 0
            else -> true
        }
        matchesSubject && matchesSearch && matchesStatus
    }

    val completedCount = filteredChapters.count { it.isCompleted }
    val totalCount = filteredChapters.size
    val completionPercent = if (totalCount > 0) (completedCount * 100) / totalCount else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Header & Summary Stats
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = "NEET Syllabus Tracker",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepBlack
                    )
                )
                Text(
                    text = "Track lectures, NCERT reading, PYQs & revisions chapter by chapter",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight)
                )
            }
        }

        // 2. Search Input Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .testTag("syllabus_search_input"),
                placeholder = {
                    Text(
                        "Search chapter (e.g. Ray Optics, Genetics, GOC...)",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMutedLight)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = LavenderDeep
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = TextMutedLight)
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LavenderPrimary,
                    unfocusedBorderColor = CardGlassBorderLight
                )
            )
        }

        // 3. Subject Filter Chips (Horizontal Scrollable)
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterTabs) { (key, label) ->
                    val isSelected = selectedSubjectFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isSelected) DeepBlack else Color.White)
                            .border(
                                BorderStroke(1.dp, if (isSelected) DeepBlack else CardGlassBorderLight),
                                RoundedCornerShape(18.dp)
                            )
                            .clickable { onFilterChange(key) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("filter_tab_$key")
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextPrimaryLight
                            )
                        )
                    }
                }
            }
        }

        // 4. Progress Banner for selected filter
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "$completedCount of $totalCount Chapters Completed ($completionPercent%)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepBlack
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        GradientProgressBar(
                            progress = completionPercent / 100f,
                            height = 8.dp,
                            gradientColors = listOf(LavenderPrimary, LavenderDeep)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    PastelPillBadge(
                        text = "$completionPercent%",
                        bgColor = LavenderSoftBg,
                        textColor = LavenderDeep
                    )
                }
            }
        }

        // 5. Chapter List Items
        items(filteredChapters, key = { it.id }) { chapter ->
            ChapterCardItem(
                chapter = chapter,
                onToggleComplete = { onToggleChapter(chapter) },
                onUpdate = onUpdateChapter,
                onClickDetails = { activeChapterForDetail = chapter },
                onAiTips = { onAskAiForChapterTips(chapter.name, chapter.subject) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    // Bottom Sheet for Chapter Details & Strategy
    if (activeChapterForDetail != null) {
        val chapter = activeChapterForDetail!!
        ModalBottomSheet(
            onDismissRequest = { activeChapterForDetail = null },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color.White
        ) {
            ChapterDetailSheetContent(
                chapter = chapter,
                onUpdate = { updated ->
                    onUpdateChapter(updated)
                    activeChapterForDetail = updated
                },
                onClose = { activeChapterForDetail = null },
                onAiTips = { onAskAiForChapterTips(chapter.name, chapter.subject) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChapterCardItem(
    chapter: ChapterEntity,
    onToggleComplete: () -> Unit,
    onUpdate: (ChapterEntity) -> Unit,
    onClickDetails: () -> Unit,
    onAiTips: () -> Unit
) {
    val subjectColor = when {
        chapter.subject == SubjectType.PHYSICS.name -> PhysicsBlue
        chapter.subject.contains("ORGANIC") -> ZoologyRose
        chapter.subject.contains("INORGANIC") -> BotanyTeal
        chapter.subject.contains("PHYSICAL") -> ChemistryOrange
        chapter.subject.contains("BOTANY") -> BotanyTeal
        chapter.subject.contains("ZOOLOGY") -> BiologyGreen
        else -> LavenderPrimary
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 22.dp,
        borderColor = if (chapter.isCompleted) SuccessGreen.copy(alpha = 0.4f) else CardGlassBorderLight
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (chapter.isCompleted) SuccessGreenLight else LavenderSoftBg)
                            .clickable(onClick = onToggleComplete),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (chapter.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                            contentDescription = "Toggle Complete",
                            tint = if (chapter.isCompleted) SuccessGreen else LavenderDeep,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = chapter.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepBlack
                            )
                        )
                        Text(
                            text = "${chapter.classLevel} • ${SubjectType.fromString(chapter.subject).displayName}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = subjectColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                // Confidence Stars
                Row {
                    (1..5).forEach { star ->
                        Icon(
                            imageVector = if (star <= chapter.confidence) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = null,
                            tint = if (star <= chapter.confidence) WarmPeach else TextMutedLight,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    onUpdate(chapter.copy(confidence = star))
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Badges: NCERT, PYQ, Formula, Revision
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // NCERT Toggle Pill
                InteractiveTogglePill(
                    label = "NCERT Read",
                    isActive = chapter.isNcertRead,
                    activeColor = SuccessGreen,
                    onToggle = { onUpdate(chapter.copy(isNcertRead = !chapter.isNcertRead)) }
                )

                // PYQ Solved Pill
                InteractiveTogglePill(
                    label = "PYQs (10 Yrs)",
                    isActive = chapter.isPyqSolved,
                    activeColor = LavenderDeep,
                    onToggle = { onUpdate(chapter.copy(isPyqSolved = !chapter.isPyqSolved)) }
                )

                // Formula Notes Pill
                InteractiveTogglePill(
                    label = "Formula Sheet",
                    isActive = chapter.isFormulaNotesReady,
                    activeColor = ChemistryOrange,
                    onToggle = { onUpdate(chapter.copy(isFormulaNotesReady = !chapter.isFormulaNotesReady)) }
                )

                // Revision count pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(WarmPeach.copy(alpha = 0.5f))
                        .clickable {
                            val nextRev = (chapter.revisionCount + 1) % 4
                            onUpdate(chapter.copy(revisionCount = nextRev))
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Rev: ${chapter.revisionCount}x 🔄",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepBlack
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Steppers for Questions and Lectures
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Question Counter Stepper
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "MCQs: ",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                    )
                    IconButton(
                        onClick = {
                            if (chapter.questionsSolved >= 10) {
                                onUpdate(chapter.copy(questionsSolved = chapter.questionsSolved - 10))
                            }
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Minus", tint = LavenderDeep, modifier = Modifier.size(14.dp))
                    }
                    Text(
                        text = "${chapter.questionsSolved}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = DeepBlack)
                    )
                    IconButton(
                        onClick = {
                            onUpdate(chapter.copy(questionsSolved = chapter.questionsSolved + 10))
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = LavenderDeep, modifier = Modifier.size(14.dp))
                    }
                }

                // AI Tips action button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LavenderSoftBg)
                        .clickable(onClick = onAiTips)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Tips",
                        tint = LavenderDeep,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "AI Tips",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = LavenderDeep
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun InteractiveTogglePill(
    label: String,
    isActive: Boolean,
    activeColor: Color,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isActive) activeColor.copy(alpha = 0.15f) else Color(0xFFF3F2F6))
            .border(
                BorderStroke(
                    1.dp,
                    if (isActive) activeColor else Color.Transparent
                ),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onToggle)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (isActive) "✓ $label" else label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (isActive) activeColor else TextSecondaryLight
                )
            )
        }
    }
}

@Composable
private fun ChapterDetailSheetContent(
    chapter: ChapterEntity,
    onUpdate: (ChapterEntity) -> Unit,
    onClose: () -> Unit,
    onAiTips: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = chapter.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepBlack
                    )
                )
                Text(
                    text = "${chapter.classLevel} • ${chapter.subject}",
                    style = MaterialTheme.typography.labelMedium.copy(color = TextSecondaryLight)
                )
            }
            PastelPillBadge(
                text = if (chapter.isCompleted) "Completed" else "In Progress",
                bgColor = if (chapter.isCompleted) SuccessGreenLight else LavenderSoftBg,
                textColor = if (chapter.isCompleted) SuccessGreen else LavenderDeep
            )
        }

        // Summary Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Preparation Checklist",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Lectures Completed: ${chapter.completedLectures}/${chapter.totalLectures}")
                    Text("MCQs: ${chapter.questionsSolved}/${chapter.targetQuestions}")
                }
                Spacer(modifier = Modifier.height(6.dp))
                GradientProgressBar(
                    progress = (chapter.completedLectures.toFloat() / maxOf(1, chapter.totalLectures)),
                    height = 8.dp,
                    gradientColors = listOf(LavenderPrimary, LavenderDeep)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GlassButton(
                text = "Get High-Yield AI Tips",
                onClick = onAiTips,
                icon = Icons.Default.AutoAwesome,
                isPrimary = true,
                modifier = Modifier.weight(1f)
            )
            GlassButton(
                text = "Done",
                onClick = onClose,
                isPrimary = false,
                modifier = Modifier.weight(0.6f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
