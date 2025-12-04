package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.entity.RecordEntity

interface RecordRepository {
    suspend fun insert(recordEntity: RecordEntity)

    suspend fun update(recordEntity: RecordEntity)

    suspend fun loadByWordId(
        bookId: Long,
        gradeId: Long,
        lessonId: Long,
        wordId: Long
    ): RecordEntity?

    suspend fun loadLessonAll(bookId: Long, gradeId: Long, lessonId: Long): List<RecordEntity>?

    suspend fun loadGradeAll(bookId: Long, gradeId: Long): List<RecordEntity>?

    suspend fun loadBookAll(bookId: Long): List<RecordEntity>?
}