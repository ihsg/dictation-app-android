package com.ihsg.dictationapp.model.repository

import com.ihsg.dictationapp.model.db.dao.BookDao
import com.ihsg.dictationapp.model.db.entity.BookEntity
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao
) : BookRepository {
    override suspend fun add(bookEntity: BookEntity) {
        bookDao.insert(bookEntity)
    }

    override suspend fun loadAll(): List<BookEntity>? {
        return bookDao.loadAll()
    }

    override suspend fun loadById(id: Long): BookEntity? {
        return bookDao.loadById(id)
    }
}