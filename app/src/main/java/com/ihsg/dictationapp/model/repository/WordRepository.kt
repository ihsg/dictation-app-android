package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.entity.WordEntity

interface WordRepository {

    suspend fun add(wordEntity: WordEntity)

    suspend fun loadAll(bookId: Long, gradeId: Long, lessonId: Long): List<WordEntity>?
}
