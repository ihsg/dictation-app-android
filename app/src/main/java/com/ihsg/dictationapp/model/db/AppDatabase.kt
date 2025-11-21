package com.ihsg.dictationapp.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ihsg.dictationapp.model.db.dao.BookDao
import com.ihsg.dictationapp.model.db.dao.GradeDao
import com.ihsg.dictationapp.model.db.dao.LessonDao
import com.ihsg.dictationapp.model.db.dao.WordDao
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.db.entity.LessonEntity
import com.ihsg.dictationapp.model.db.entity.WordEntity

@Database(
    entities = [
        BookEntity::class,
        GradeEntity::class,
        LessonEntity::class,
        WordEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun gradeDao(): GradeDao
    abstract fun lessonDao(): LessonDao
    abstract fun wordDao(): WordDao
}