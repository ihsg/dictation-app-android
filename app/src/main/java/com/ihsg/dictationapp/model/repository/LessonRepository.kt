package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.entity.LessonEntity

interface LessonRepository {

    suspend fun add(lessonEntity: LessonEntity)

    suspend fun loadAll(bookId: Long, gradeId: Long): List<LessonEntity>?

    suspend fun loadById(bookId: Long, gradeId: Long, id: Long): LessonEntity?
}
