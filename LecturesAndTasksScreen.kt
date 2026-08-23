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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HabitEntity
import com.example.model.LectureEntity
import com.example.model.TaskEntity
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

@Composable
fun LecturesAndTasksScreen(
    lectures: List<LectureEntity>,
    tasks: List<TaskEntity>,
    habits: List<HabitEntity>,
    onToggleLecture: (LectureEntity) -> Unit,
    onAddLecture: (String, String, String, String, Int) -> Unit,
    onDeleteLecture: (LectureEntity) -> Unit,
    onToggleTask: (TaskEntity) -> Unit,
    onAddTask: (String, String, String) -> Unit,
    onDeleteTask: (TaskEntity) -> Unit,
    onToggleHabit: (HabitEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubSection by remember { mutableStateOf(0) } // 0: Lectures, 1: Daily Tasks, 2: Habit Tracker
    var showAddLectureDialog by remember { mutableStateOf(false) }
    var showAddTaskDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Section Selector Pill Tabs
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
                val tabs = listOf("Lectures (${lectures.size})", "To-Do (${tasks.count { !it.isCompleted }})", "Habits (7d)")
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedSubSection == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) DeepBlack else Color.Transparent)
                            .clickable { selectedSubSection = index }
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

        when (selectedSubSection) {
            0 -> {
                // LECTURES TRACKER
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Daily Lectures",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Track playback progress & faculty sessions",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                            )
                        }
                        GlassButton(
                            text = "+ Lecture",
                            onClick = { showAddLectureDialog = true },
                            isPrimary = true,
                            testTag = "add_lecture_btn"
                        )
                    }
                }

                items(lectures, key = { it.id }) { lecture ->
                    LectureCardItem(
                        lecture = lecture,
                        onToggle = { onToggleLecture(lecture) },
                        onDelete = { onDeleteLecture(lecture) }
                    )
                }
            }

            1 -> {
                // DAILY TO-DO TASKS
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Daily Preparation To-Dos",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Prioritize NCERT reading, numericals & test analysis",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                            )
                        }
                        GlassButton(
                            text = "+ Task",
                            onClick = { showAddTaskDialog = true },
                            isPrimary = true,
                            testTag = "add_task_btn"
                        )
                    }
                }

                items(tasks, key = { it.id }) { task ->
                    TaskCardItem(
                        task = task,
                        onToggle = { onToggleTask(task) },
                        onDelete = { onDeleteTask(task) }
                    )
                }
            }

            2 -> {
                // HABIT TRACKER MATRIX
                item {
                    Column {
                        Text(
                            text = "7-Day Discipline Matrix",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Non-negotiable NEET daily consistency rituals",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                        )
                    }
                }

                items(habits, key = { it.id }) { habit ->
                    HabitMatrixCardItem(
                        habit = habit,
                        onToggleToday = { onToggleHabit(habit) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    if (showAddLectureDialog) {
        AddLectureDialog(
            onDismiss = { showAddLectureDialog = false },
            onConfirm = { chapter, subject, faculty, topic, dur ->
                onAddLecture(chapter, subject, faculty, topic, dur)
                showAddLectureDialog = false
            }
        )
    }

    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onConfirm = { title, subject, priority ->
                onAddTask(title, subject, priority)
                showAddTaskDialog = false
            }
        )
    }
}

@Composable
private fun LectureCardItem(
    lecture: LectureEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val subjectColor = when (lecture.subject.uppercase()) {
        "PHYSICS" -> PhysicsBlue
        "CHEMISTRY", "CHEMISTRY_ORGANIC", "CHEMISTRY_PHYSICAL", "CHEMISTRY_INORGANIC" -> ChemistryOrange
        else -> BiologyGreen
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 22.dp,
        borderColor = if (lecture.isCompleted) SuccessGreen.copy(alpha = 0.4f) else CardGlassBorderLight
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (lecture.isCompleted) SuccessGreenLight else LavenderSoftBg)
                            .clickable(onClick = onToggle),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (lecture.isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = if (lecture.isCompleted) SuccessGreen else LavenderDeep,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = lecture.chapterName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepBlack
                            )
                        )
                        Text(
                            text = "${lecture.facultyName} • ${lecture.durationMinutes} mins",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                        )
                    }
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = TextMutedLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = lecture.topic,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimaryLight,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GradientProgressBar(
                    progress = (lecture.watchedPercentage / 100f),
                    height = 6.dp,
                    gradientColors = listOf(subjectColor, subjectColor.copy(alpha = 0.8f)),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${lecture.watchedPercentage}%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = subjectColor
                    )
                )
            }
        }
    }
}

@Composable
private fun TaskCardItem(
    task: TaskEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp,
        borderColor = if (task.isCompleted) SuccessGreen.copy(alpha = 0.3f) else CardGlassBorderLight
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .clickable(onClick = onToggle),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                contentDescription = null,
                tint = if (task.isCompleted) SuccessGreen else LavenderPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (task.isCompleted) TextMutedLight else TextPrimaryLight,
                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                )
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    PastelPillBadge(
                        text = task.subject,
                        bgColor = LavenderSoftBg,
                        textColor = LavenderDeep
                    )
                    Spacer(modifier = Modifier.width(6.dp))
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

            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = TextMutedLight,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun HabitMatrixCardItem(
    habit: HabitEntity,
    onToggleToday: () -> Unit
) {
    val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 22.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepBlack
                        )
                    )
                    Text(
                        text = "${habit.targetDaysPerWeek} days target • Weekly streak: ${habit.currentStreak}d 🔥",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (habit.isTodayCompleted) SuccessGreen else LavenderSoftBg)
                        .clickable(onClick = onToggleToday)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (habit.isTodayCompleted) "Completed ✓" else "Mark Today",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (habit.isTodayCompleted) Color.White else DeepBlack
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 7 Days Pill Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dayLabels.forEachIndexed { index, label ->
                    val isDone = ((habit.historyMask shr (6 - index)) and 1) == 1
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMutedLight,
                                fontSize = 10.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isDone) LavenderPrimary else Color(0xFFF1EFF7)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = DeepBlack,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddLectureDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Int) -> Unit
) {
    var chapterName by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Physics") }
    var facultyName by remember { mutableStateOf("MR Sir") }
    var topic by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("60") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Lecture", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = chapterName,
                    onValueChange = { chapterName = it },
                    label = { Text("Chapter Name") },
                    placeholder = { Text("e.g. Ray Optics") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Topic / Lecture Title") },
                    placeholder = { Text("e.g. Lec 05: Prism & Dispersion") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = facultyName,
                    onValueChange = { facultyName = it },
                    label = { Text("Faculty / Platform") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duration (minutes)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (chapterName.isNotBlank() && topic.isNotBlank()) {
                        onConfirm(chapterName, subject, facultyName, topic, duration.toIntOrNull() ?: 60)
                    }
                }
            ) {
                Text("Add Lecture", fontWeight = FontWeight.Bold, color = LavenderDeep)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Biology") }
    var priority by remember { mutableStateOf("HIGH") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Daily Preparation Task", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Description") },
                    placeholder = { Text("e.g. Solve 50 Physics PYQs") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject / Area") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, subject, priority)
                    }
                }
            ) {
                Text("Add Task", fontWeight = FontWeight.Bold, color = LavenderDeep)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
