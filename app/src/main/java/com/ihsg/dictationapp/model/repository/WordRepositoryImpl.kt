package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.dao.WordDao
import com.ihsg.dictationapp.model.db.entity.WordEntity
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(
    private val wordDao: WordDao
) : WordRepository {
    override suspend fun add(wordEntity: WordEntity) {
        wordDao.insert(wordEntity)
    }

    override suspend fun loadAll(bookId: Long, gradeId: Long, lessonId: Long): List<WordEntity>? {
        return wordDao.loadAll(bookId, gradeId, lessonId)
    }
}