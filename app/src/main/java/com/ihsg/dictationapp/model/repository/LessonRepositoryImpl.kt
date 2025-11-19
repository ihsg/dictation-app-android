package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.dao.LessonDao
import com.ihsg.dictationapp.model.db.entity.LessonEntity
import javax.inject.Inject

class LessonRepositoryImpl @Inject constructor(
    private val lessonDao: LessonDao
) : LessonRepository {
    override suspend fun add(lessonEntity: LessonEntity) {
        lessonDao.insert(lessonEntity)
    }

    override suspend fun loadAll(bookId: Long, gradeId: Long): List<LessonEntity>? {
        return lessonDao.loadAll(bookId, gradeId)
    }

    override suspend fun loadById(bookId: Long, gradeId: Long, id: Long): LessonEntity? {
        return lessonDao.loadById(bookId, gradeId, id)
    }
}