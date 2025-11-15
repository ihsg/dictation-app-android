package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.dao.GradeDao
import com.ihsg.dictationapp.model.db.entity.GradeEntity
import javax.inject.Inject

class GradeRepositoryImpl @Inject constructor(
    private val gradeDao: GradeDao
) : GradeRepository {
    override suspend fun add(gradeEntity: GradeEntity) {
        gradeDao.insert(gradeEntity)
    }

    override suspend fun loadAll(bookId: Long): List<GradeEntity>? {
        return gradeDao.loadAll(bookId)
    }

    override suspend fun loadById(bookId: Long, id: Long): GradeEntity? {
        return gradeDao.loadById(bookId, id)
    }
}