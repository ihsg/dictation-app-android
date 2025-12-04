package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.dao.RecordDao
import com.ihsg.dictationapp.model.db.entity.RecordEntity
import javax.inject.Inject


class RecordRepositoryImpl @Inject constructor(
    private val recordDao: RecordDao
) : RecordRepository {

    override suspend fun insert(recordEntity: RecordEntity) {
        recordDao.insert(recordEntity)
    }

    override suspend fun update(recordEntity: RecordEntity) {
        recordDao.update(recordEntity)
    }

    override suspend fun loadByWordId(
        bookId: Long,
        gradeId: Long,
        lessonId: Long,
        wordId: Long
    ): RecordEntity? {
        return recordDao.loadByWordId(bookId, gradeId, lessonId, wordId)
    }

    override suspend fun loadLessonAll(
        bookId: Long,
        gradeId: Long,
        lessonId: Long
    ): List<RecordEntity>? {
        return recordDao.loadLessonAll(bookId, gradeId, lessonId)
    }

    override suspend fun loadGradeAll(bookId: Long, gradeId: Long): List<RecordEntity>? {
        return recordDao.loadGradeAll(bookId, gradeId)
    }

    override suspend fun loadBookAll(bookId: Long): List<RecordEntity>? {
        return recordDao.loadBookAll(bookId)
    }
}