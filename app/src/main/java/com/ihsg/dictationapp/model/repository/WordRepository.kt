package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.entity.WordEntity

interface WordRepository {

    suspend fun add(wordEntity: WordEntity)

    suspend fun loadAll(lessonId: Long): List<WordEntity>?
}
