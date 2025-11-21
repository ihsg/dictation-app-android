package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.entity.BookEntity

interface BookRepository {

    suspend fun add(bookEntity: BookEntity)

    suspend fun loadAll(): List<BookEntity>?

    suspend fun loadById(id: Long): BookEntity?
}