package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.entity.LessonEntity

interface LessonRepository {

    suspend fun add(lessonEntity: LessonEntity)

    suspend fun loadAll(gradeId: Long): List<LessonEntity>?

    suspend fun loadById(gradeId: Long, id: Long): LessonEntity?
}
