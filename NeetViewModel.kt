package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiClient
import com.example.data.InitialSyllabusData
import com.example.data.NeetRepository
import com.example.model.ChapterEntity
import com.example.model.ErrorNoteEntity
import com.example.model.HabitEntity
import com.example.model.LectureEntity
import com.example.model.MockTestEntity
import com.example.model.StudySessionEntity
import com.example.model.SubjectType
import com.example.model.TaskEntity
import com.example.model.UserProfileEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NeetViewModel(private val repository: NeetRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.checkAndPrepopulate()
            fetchAiInsight()
        }
    }

    val chapters: StateFlow<List<ChapterEntity>> = repository.allChapters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lectures: StateFlow<List<LectureEntity>> = repository.allLectures
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<TaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habits: StateFlow<List<HabitEntity>> = repository.allHabits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sessions: StateFlow<List<StudySessionEntity>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mockTests: StateFlow<List<MockTestEntity>> = repository.allMockTests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val errorNotes: StateFlow<List<ErrorNoteEntity>> = repository.allErrorNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialSyllabusData.getDefaultProfile())

    // UI Navigation State
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun selectTab(tab: Int) {
        _selectedTab.value = tab
    }

    // Syllabus Filter State
    private val _syllabusSearchQuery = MutableStateFlow("")
    val syllabusSearchQuery: StateFlow<String> = _syllabusSearchQuery.asStateFlow()

    private val _selectedSubjectFilter = MutableStateFlow("ALL")
    val selectedSubjectFilter: StateFlow<String> = _selectedSubjectFilter.asStateFlow()

    fun setSyllabusSearchQuery(query: String) {
        _syllabusSearchQuery.value = query
    }

    fun setSubjectFilter(filter: String) {
        _selectedSubjectFilter.value = filter
    }

    // AI Insight State
    private val _aiInsight = MutableStateFlow("Analyzing your NEET readiness metrics...")
    val aiInsight: StateFlow<String> = _aiInsight.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    fun fetchAiInsight() {
        viewModelScope.launch {
            _isAiLoading.value = true
            val currentChapters = chapters.value
            val profile = userProfile.value ?: InitialSyllabusData.getDefaultProfile()
            val latestMock = mockTests.value.firstOrNull()?.totalScore ?: 668

            val total = if (currentChapters.isNotEmpty()) currentChapters.size else 1
            val completed = currentChapters.count { it.isCompleted }
            val overallPercent = (completed * 100) / total

            val phy = currentChapters.filter { it.subject == SubjectType.PHYSICS.name }
            val phyPercent = if (phy.isNotEmpty()) (phy.count { it.isCompleted } * 100) / phy.size else 0

            val chem = currentChapters.filter { it.subject.startsWith("CHEMISTRY") }
            val chemPercent = if (chem.isNotEmpty()) (chem.count { it.isCompleted } * 100) / chem.size else 0

            val bio = currentChapters.filter { it.subject.startsWith("BIOLOGY") }
            val bioPercent = if (bio.isNotEmpty()) (bio.count { it.isCompleted } * 100) / bio.size else 0

            val result = GeminiClient.getStudyCoachInsight(
                streakDays = profile.currentStreak,
                totalHours = profile.totalHoursStudied,
                mockScore = latestMock,
                syllabusCompletionPercent = overallPercent,
                physicsCompletion = phyPercent,
                chemistryCompletion = chemPercent,
                biologyCompletion = bioPercent
            )
            _aiInsight.value = result
            _isAiLoading.value = false
        }
    }

    // Timer (Pomodoro) State
    private val _timerMode = MutableStateFlow("25/5")
    val timerMode: StateFlow<String> = _timerMode.asStateFlow()

    private val _timerSubject = MutableStateFlow("Physics")
    val timerSubject: StateFlow<String> = _timerSubject.asStateFlow()

    private val _timerRemainingSeconds = MutableStateFlow(25 * 60)
    val timerRemainingSeconds: StateFlow<Int> = _timerRemainingSeconds.asStateFlow()

    private val _timerTotalSeconds = MutableStateFlow(25 * 60)
    val timerTotalSeconds: StateFlow<Int> = _timerTotalSeconds.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null

    fun setTimerSubject(subject: String) {
        _timerSubject.value = subject
    }

    fun setTimerMode(mode: String, customMinutes: Int = 25) {
        _timerMode.value = mode
        val totalSecs = when (mode) {
            "25/5" -> 25 * 60
            "50/10" -> 50 * 60
            else -> customMinutes * 60
        }
        _timerTotalSeconds.value = totalSecs
        _timerRemainingSeconds.value = totalSecs
        pauseTimer()
    }

    fun toggleTimer() {
        if (_isTimerRunning.value) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    fun startTimer() {
        if (_timerRemainingSeconds.value <= 0) {
            _timerRemainingSeconds.value = _timerTotalSeconds.value
        }
        _isTimerRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerRemainingSeconds.value > 0 && _isTimerRunning.value) {
                delay(1000)
                _timerRemainingSeconds.value -= 1
            }
            if (_timerRemainingSeconds.value == 0) {
                finishTimerSession()
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _timerRemainingSeconds.value = _timerTotalSeconds.value
    }

    fun finishTimerSession() {
        pauseTimer()
        val durationMins = _timerTotalSeconds.value / 60
        viewModelScope.launch {
            repository.recordStudySession(
                StudySessionEntity(
                    subject = _timerSubject.value,
                    chapterName = "Focused NEET Study",
                    durationMinutes = durationMins,
                    mode = _timerMode.value
                )
            )
            _timerRemainingSeconds.value = _timerTotalSeconds.value
        }
    }

    // Chapter Operations
    fun toggleChapterCompleted(chapter: ChapterEntity) {
        viewModelScope.launch {
            val newCompleted = !chapter.isCompleted
            val newLecs = if (newCompleted) chapter.totalLectures else chapter.completedLectures
            repository.updateChapter(
                chapter.copy(
                    isCompleted = newCompleted,
                    completedLectures = newLecs,
                    lastStudiedTimestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun updateChapterDetails(chapter: ChapterEntity) {
        viewModelScope.launch {
            repository.updateChapter(chapter)
        }
    }

    // Lecture Operations
    fun toggleLectureComplete(lecture: LectureEntity) {
        viewModelScope.launch {
            val newStatus = !lecture.isCompleted
            repository.updateLecture(
                lecture.copy(
                    isCompleted = newStatus,
                    watchedPercentage = if (newStatus) 100 else 50
                )
            )
        }
    }

    fun addLecture(chapterName: String, subject: String, faculty: String, topic: String, duration: Int) {
        viewModelScope.launch {
            repository.insertLecture(
                LectureEntity(
                    chapterName = chapterName,
                    subject = subject,
                    facultyName = faculty,
                    topic = topic,
                    durationMinutes = duration,
                    watchedPercentage = 0,
                    isCompleted = false,
                    dateString = "Today"
                )
            )
        }
    }

    fun deleteLecture(lecture: LectureEntity) {
        viewModelScope.launch {
            repository.deleteLecture(lecture)
        }
    }

    // Task Operations
    fun toggleTaskComplete(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun addTask(title: String, subject: String, priority: String) {
        viewModelScope.launch {
            repository.insertTask(
                TaskEntity(
                    title = title,
                    subject = subject,
                    priority = priority,
                    isCompleted = false,
                    dueDate = "Today"
                )
            )
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    // Habit Operations
    fun toggleHabitToday(habit: HabitEntity) {
        viewModelScope.launch {
            val newToday = !habit.isTodayCompleted
            val newStreak = if (newToday) habit.currentStreak + 1 else maxOf(0, habit.currentStreak - 1)
            val newMask = if (newToday) (habit.historyMask or 1) else (habit.historyMask and 1.inv())
            repository.updateHabit(
                habit.copy(
                    isTodayCompleted = newToday,
                    currentStreak = newStreak,
                    historyMask = newMask
                )
            )
        }
    }

    // Mock Test Operations
    fun addMockTest(name: String, phy: Int, chem: Int, bio: Int, accuracy: Float, dateStr: String, notes: String) {
        viewModelScope.launch {
            val total = phy + chem + bio
            val percentile = 85.0f + (total.toFloat() / 720f) * 14.9f
            repository.insertMockTest(
                MockTestEntity(
                    testName = name,
                    dateString = dateStr,
                    physicsScore = phy,
                    chemistryScore = chem,
                    biologyScore = bio,
                    totalScore = total,
                    accuracyPercent = accuracy,
                    percentile = percentile,
                    notes = notes
                )
            )
        }
    }

    fun deleteMockTest(test: MockTestEntity) {
        viewModelScope.launch {
            repository.deleteMockTest(test)
        }
    }

    // Error Notebook Operations
    fun addErrorNote(subject: String, chapter: String, summary: String, mistake: String, concept: String, tag: String) {
        viewModelScope.launch {
            repository.insertErrorNote(
                ErrorNoteEntity(
                    subject = subject,
                    chapterName = chapter,
                    questionSummary = summary,
                    mistakeDescription = mistake,
                    correctConcept = concept,
                    status = "UNSOLVED",
                    tag = tag
                )
            )
        }
    }

    fun updateErrorNoteStatus(note: ErrorNoteEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateErrorNote(note.copy(status = newStatus))
        }
    }

    fun deleteErrorNote(note: ErrorNoteEntity) {
        viewModelScope.launch {
            repository.deleteErrorNote(note)
        }
    }

    // User Profile
    fun updateUserTarget(targetScore: Int, college: String, name: String) {
        viewModelScope.launch {
            val curr = userProfile.value ?: InitialSyllabusData.getDefaultProfile()
            repository.updateUserProfile(curr.copy(targetScore = targetScore, targetCollege = college, name = name))
        }
    }
}

class NeetViewModelFactory(private val repository: NeetRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NeetViewModel::class.java)) {
            return NeetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
