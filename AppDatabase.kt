package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.model.ChapterEntity
import com.example.model.ErrorNoteEntity
import com.example.model.HabitEntity
import com.example.model.LectureEntity
import com.example.model.MockTestEntity
import com.example.model.StudySessionEntity
import com.example.model.TaskEntity
import com.example.model.UserProfileEntity

@Database(
    entities = [
        ChapterEntity::class,
        LectureEntity::class,
        TaskEntity::class,
        HabitEntity::class,
        StudySessionEntity::class,
        MockTestEntity::class,
        ErrorNoteEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chapterDao(): ChapterDao
    abstract fun lectureDao(): LectureDao
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun mockTestDao(): MockTestDao
    abstract fun errorNoteDao(): ErrorNoteDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "neetracker_database.db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
