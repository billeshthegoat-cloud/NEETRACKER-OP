package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SubjectType(val displayName: String, val category: String) {
    PHYSICS("Physics", "Physics"),
    CHEMISTRY_PHYSICAL("Physical Chemistry", "Chemistry"),
    CHEMISTRY_ORGANIC("Organic Chemistry", "Chemistry"),
    CHEMISTRY_INORGANIC("Inorganic Chemistry", "Chemistry"),
    BIOLOGY_BOTANY("Botany", "Biology"),
    BIOLOGY_ZOOLOGY("Zoology", "Biology");

    companion object {
        fun fromString(value: String): SubjectType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: PHYSICS
        }
    }
}

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val subject: String, // SubjectType name
    val classLevel: String = "Class 12", // "Class 11" or "Class 12"
    val totalLectures: Int = 8,
    val completedLectures: Int = 0,
    val questionsSolved: Int = 0,
    val targetQuestions: Int = 150,
    val revisionCount: Int = 0,
    val confidence: Int = 3, // 1 to 5
    val isNcertRead: Boolean = false,
    val isPyqSolved: Boolean = false,
    val isFormulaNotesReady: Boolean = false,
    val isCompleted: Boolean = false,
    val notes: String = "",
    val lastStudiedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "lectures")
data class LectureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val chapterName: String,
    val subject: String,
    val facultyName: String = "MR Sir",
    val topic: String,
    val durationMinutes: Int = 60,
    val watchedPercentage: Int = 0,
    val isCompleted: Boolean = false,
    val dateString: String = "Today",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val subject: String = "Biology",
    val priority: String = "HIGH", // "HIGH", "MEDIUM", "LOW"
    val isCompleted: Boolean = false,
    val dueDate: String = "Today",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iconKey: String = "book", // "sun", "clock", "book", "pencil", "water", "fire", "moon"
    val targetDaysPerWeek: Int = 7,
    val currentStreak: Int = 0,
    val historyMask: Int = 0, // Bitmask for past 7 days (bit 0 = today, bit 1 = yesterday, etc.)
    val isTodayCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subject: String,
    val chapterName: String,
    val durationMinutes: Int,
    val mode: String = "25/5",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mock_tests")
data class MockTestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val testName: String,
    val dateString: String,
    val physicsScore: Int, // max 180
    val chemistryScore: Int, // max 180
    val biologyScore: Int, // max 360
    val totalScore: Int, // max 720
    val accuracyPercent: Float = 85.0f,
    val percentile: Float = 98.4f,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "error_notes")
data class ErrorNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subject: String,
    val chapterName: String,
    val questionSummary: String,
    val mistakeDescription: String,
    val correctConcept: String,
    val status: String = "REVIEWING", // "UNSOLVED", "REVIEWING", "MASTERED"
    val tag: String = "Concept Trap", // "Calculation", "Concept Trap", "Formula Slip", "NCERT Line"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val name: String = "Aspirant Aryan",
    val targetScore: Int = 685,
    val targetCollege: String = "AIIMS New Delhi",
    val currentStreak: Int = 185,
    val totalHoursStudied: Float = 142.5f,
    val totalXp: Int = 4850,
    val level: Int = 12,
    val dailyGoalHours: Float = 8.0f,
    val unlockedBadges: String = "STREAK_100,BIO_CHAMP,NIGHT_OWL,QUESTION_CRUSHER" // Comma-separated
)
