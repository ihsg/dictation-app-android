package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.entity.GradeEntity

interface GradeRepository {

    suspend fun add(gradeEntity: GradeEntity)

    suspend fun loadAll(bookId: Long): List<GradeEntity>?

    suspend fun loadById(bookId: Long, gradeId: Long): GradeEntity?
}
