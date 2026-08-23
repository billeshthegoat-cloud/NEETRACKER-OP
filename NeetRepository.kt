package com.example.data

import com.example.model.ChapterEntity
import com.example.model.ErrorNoteEntity
import com.example.model.HabitEntity
import com.example.model.LectureEntity
import com.example.model.MockTestEntity
import com.example.model.StudySessionEntity
import com.example.model.TaskEntity
import com.example.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NeetRepository(private val database: AppDatabase) {
    private val chapterDao = database.chapterDao()
    private val lectureDao = database.lectureDao()
    private val taskDao = database.taskDao()
    private val habitDao = database.habitDao()
    private val studySessionDao = database.studySessionDao()
    private val mockTestDao = database.mockTestDao()
    private val errorNoteDao = database.errorNoteDao()
    private val userProfileDao = database.userProfileDao()

    val allChapters: Flow<List<ChapterEntity>> = chapterDao.getAllChapters()
    val allLectures: Flow<List<LectureEntity>> = lectureDao.getAllLectures()
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    val allHabits: Flow<List<HabitEntity>> = habitDao.getAllHabits()
    val allSessions: Flow<List<StudySessionEntity>> = studySessionDao.getAllSessions()
    val allMockTests: Flow<List<MockTestEntity>> = mockTestDao.getAllMockTests()
    val allErrorNotes: Flow<List<ErrorNoteEntity>> = errorNoteDao.getAllErrorNotes()
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()

    suspend fun checkAndPrepopulate() {
        if (chapterDao.getChapterCount() == 0) {
            chapterDao.insertChapters(InitialSyllabusData.getDefaultChapters())
        }
        if (lectureDao.getLectureCount() == 0) {
            lectureDao.insertLectures(InitialSyllabusData.getDefaultLectures())
        }
        if (taskDao.getTaskCount() == 0) {
            taskDao.insertTasks(InitialSyllabusData.getDefaultTasks())
        }
        if (habitDao.getHabitCount() == 0) {
            habitDao.insertHabits(InitialSyllabusData.getDefaultHabits())
        }
        if (mockTestDao.getMockTestCount() == 0) {
            mockTestDao.insertMockTests(InitialSyllabusData.getDefaultMockTests())
        }
        if (errorNoteDao.getErrorNoteCount() == 0) {
            errorNoteDao.insertErrorNotes(InitialSyllabusData.getDefaultErrorNotes())
        }
        if (studySessionDao.getSessionCount() == 0) {
            studySessionDao.insertSessions(InitialSyllabusData.getDefaultSessions())
        }
        if (userProfileDao.getProfileCount() == 0) {
            userProfileDao.insertUserProfile(InitialSyllabusData.getDefaultProfile())
        }
    }

    suspend fun updateChapter(chapter: ChapterEntity) {
        chapterDao.updateChapter(chapter)
    }

    suspend fun insertLecture(lecture: LectureEntity) {
        lectureDao.insertLecture(lecture)
        addXp(50)
    }

    suspend fun updateLecture(lecture: LectureEntity) {
        lectureDao.updateLecture(lecture)
        if (lecture.isCompleted) {
            addXp(30)
        }
    }

    suspend fun deleteLecture(lecture: LectureEntity) {
        lectureDao.deleteLecture(lecture)
    }

    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
        if (task.isCompleted) {
            addXp(25)
        }
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    suspend fun updateHabit(habit: HabitEntity) {
        habitDao.updateHabit(habit)
        if (habit.isTodayCompleted) {
            addXp(20)
        }
    }

    suspend fun recordStudySession(session: StudySessionEntity) {
        studySessionDao.insertSession(session)
        val hoursGained = session.durationMinutes / 60f
        val xpGained = (session.durationMinutes * 2)
        addXpAndHours(xpGained, hoursGained)
    }

    suspend fun insertMockTest(test: MockTestEntity) {
        mockTestDao.insertMockTest(test)
        addXp(150)
    }

    suspend fun deleteMockTest(test: MockTestEntity) {
        mockTestDao.deleteMockTest(test)
    }

    suspend fun insertErrorNote(note: ErrorNoteEntity) {
        errorNoteDao.insertErrorNote(note)
        addXp(40)
    }

    suspend fun updateErrorNote(note: ErrorNoteEntity) {
        errorNoteDao.updateErrorNote(note)
    }

    suspend fun deleteErrorNote(note: ErrorNoteEntity) {
        errorNoteDao.deleteErrorNote(note)
    }

    suspend fun updateUserProfile(profile: UserProfileEntity) {
        userProfileDao.updateUserProfile(profile)
    }

    private suspend fun addXp(xp: Int) {
        val current = userProfile.firstOrNull() ?: InitialSyllabusData.getDefaultProfile()
        val newXp = current.totalXp + xp
        val newLevel = (newXp / 400) + 1
        userProfileDao.updateUserProfile(current.copy(totalXp = newXp, level = newLevel))
    }

    private suspend fun addXpAndHours(xp: Int, hours: Float) {
        val current = userProfile.firstOrNull() ?: InitialSyllabusData.getDefaultProfile()
        val newXp = current.totalXp + xp
        val newHours = current.totalHoursStudied + hours
        val newLevel = (newXp / 400) + 1
        userProfileDao.updateUserProfile(current.copy(totalXp = newXp, totalHoursStudied = newHours, level = newLevel))
    }
}
