package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.data.AppDatabase
import com.example.data.NeetRepository
import com.example.ui.components.AiCoachBottomSheet
import com.example.ui.components.FloatingQuickActionsMenu
import com.example.ui.components.NeetBottomDock
import com.example.ui.components.NeetTopAppBar
import com.example.ui.components.ProfileDialog
import com.example.ui.components.SoftMeshBackground
import com.example.ui.screens.AnalyticsAndErrorsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LecturesAndTasksScreen
import com.example.ui.screens.StudyTimerScreen
import com.example.ui.screens.SyllabusScreen
import com.example.ui.theme.NeetrackerTheme
import com.example.viewmodel.NeetViewModel
import com.example.viewmodel.NeetViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: NeetViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = NeetRepository(db)
        NeetViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NeetrackerTheme {
                NeetTrackerApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NeetTrackerApp(viewModel: NeetViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val chapters by viewModel.chapters.collectAsState()
    val lectures by viewModel.lectures.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val mockTests by viewModel.mockTests.collectAsState()
    val errorNotes by viewModel.errorNotes.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val syllabusSearchQuery by viewModel.syllabusSearchQuery.collectAsState()
    val selectedSubjectFilter by viewModel.selectedSubjectFilter.collectAsState()
    val aiInsight by viewModel.aiInsight.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    val timerRemaining by viewModel.timerRemainingSeconds.collectAsState()
    val timerTotal by viewModel.timerTotalSeconds.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val timerMode by viewModel.timerMode.collectAsState()
    val timerSubject by viewModel.timerSubject.collectAsState()

    var showAiCoachSheet by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var chapterTipQuery by remember { mutableStateOf<Pair<String, String>?>(null) }

    SoftMeshBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                NeetTopAppBar(
                    userProfile = userProfile,
                    onProfileClick = { showProfileDialog = true },
                    onAiCoachClick = {
                        chapterTipQuery = null
                        showAiCoachSheet = true
                    }
                )
            },
            bottomBar = {
                NeetBottomDock(
                    selectedTab = selectedTab,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                // Screen Content with Animated Transition
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "screen_transition"
                ) { tab ->
                    when (tab) {
                        0 -> DashboardScreen(
                            userProfile = userProfile,
                            chapters = chapters,
                            lectures = lectures,
                            tasks = tasks,
                            habits = habits,
                            aiInsight = aiInsight,
                            isAiLoading = isAiLoading,
                            onRefreshAi = { viewModel.fetchAiInsight() },
                            onNavigateToSyllabus = { viewModel.selectTab(1) },
                            onNavigateToTimer = { viewModel.selectTab(3) },
                            onNavigateToLectures = { viewModel.selectTab(2) },
                            onToggleTask = { viewModel.toggleTaskComplete(it) },
                            onToggleHabit = { viewModel.toggleHabitToday(it) },
                            onOpenAiCoachDialog = {
                                chapterTipQuery = null
                                showAiCoachSheet = true
                            }
                        )

                        1 -> SyllabusScreen(
                            chapters = chapters,
                            searchQuery = syllabusSearchQuery,
                            selectedSubjectFilter = selectedSubjectFilter,
                            onSearchChange = { viewModel.setSyllabusSearchQuery(it) },
                            onFilterChange = { viewModel.setSubjectFilter(it) },
                            onToggleChapter = { viewModel.toggleChapterCompleted(it) },
                            onUpdateChapter = { viewModel.updateChapterDetails(it) },
                            onAskAiForChapterTips = { name, subj ->
                                chapterTipQuery = Pair(name, subj)
                                showAiCoachSheet = true
                            }
                        )

                        2 -> LecturesAndTasksScreen(
                            lectures = lectures,
                            tasks = tasks,
                            habits = habits,
                            onToggleLecture = { viewModel.toggleLectureComplete(it) },
                            onAddLecture = { chap, subj, fac, top, dur ->
                                viewModel.addLecture(chap, subj, fac, top, dur)
                            },
                            onDeleteLecture = { viewModel.deleteLecture(it) },
                            onToggleTask = { viewModel.toggleTaskComplete(it) },
                            onAddTask = { title, subj, prio ->
                                viewModel.addTask(title, subj, prio)
                            },
                            onDeleteTask = { viewModel.deleteTask(it) },
                            onToggleHabit = { viewModel.toggleHabitToday(it) }
                        )

                        3 -> StudyTimerScreen(
                            timerRemainingSeconds = timerRemaining,
                            timerTotalSeconds = timerTotal,
                            isTimerRunning = isTimerRunning,
                            timerMode = timerMode,
                            timerSubject = timerSubject,
                            sessions = sessions,
                            onToggleTimer = { viewModel.toggleTimer() },
                            onResetTimer = { viewModel.resetTimer() },
                            onFinishSession = { viewModel.finishTimerSession() },
                            onSetMode = { viewModel.setTimerMode(it) },
                            onSetSubject = { viewModel.setTimerSubject(it) }
                        )

                        4 -> AnalyticsAndErrorsScreen(
                            mockTests = mockTests,
                            errorNotes = errorNotes,
                            userProfile = userProfile,
                            onAddMockTest = { name, phy, chem, bio, acc, date, notes ->
                                viewModel.addMockTest(name, phy, chem, bio, acc, date, notes)
                            },
                            onDeleteMockTest = { viewModel.deleteMockTest(it) },
                            onAddErrorNote = { subj, chap, sum, mis, con, tag ->
                                viewModel.addErrorNote(subj, chap, sum, mis, con, tag)
                            },
                            onUpdateErrorStatus = { note, stat ->
                                viewModel.updateErrorNoteStatus(note, stat)
                            },
                            onDeleteErrorNote = { viewModel.deleteErrorNote(it) }
                        )
                    }
                }

                // Floating Speed-Dial Action Button
                FloatingQuickActionsMenu(
                    onStartTimer = {
                        viewModel.selectTab(3)
                        viewModel.startTimer()
                    },
                    onAddLecture = {
                        viewModel.selectTab(2)
                    },
                    onAddTask = {
                        viewModel.selectTab(2)
                    },
                    onLogError = {
                        viewModel.selectTab(4)
                    },
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }

        // Modals & Bottom Sheets
        if (showAiCoachSheet) {
            AiCoachBottomSheet(
                onDismiss = {
                    showAiCoachSheet = false
                    chapterTipQuery = null
                },
                userProfile = userProfile,
                onUpdateProfile = { score, col, name ->
                    viewModel.updateUserTarget(score, col, name)
                },
                initialChapterTipsPrompt = chapterTipQuery
            )
        }

        if (showProfileDialog) {
            ProfileDialog(
                userProfile = userProfile,
                onDismiss = { showProfileDialog = false },
                onSave = { score, college, name ->
                    viewModel.updateUserTarget(score, college, name)
                }
            )
        }
    }
}
