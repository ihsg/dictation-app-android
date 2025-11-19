package com.ihsg.dictationapp.data

import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.repository.BookRepository
import java.util.Locale
import javax.inject.Inject

class BookDataFactory @Inject constructor(
    private val bookRepository: BookRepository,
    private val chGradeDataFactory: ChGradeDataFactory,
    private val enGradeDataFactory: EnGradeDataFactory,
) : DataFactory {
    private val logger = Logger(this)

    override suspend fun buildData() {
        logger.d { "buildData called" }

        val books = listOf(
            BookEntity(
                name = "江苏人教版小学语文",
                language = Locale.CHINESE.toString()
            ),
            BookEntity(
                name = "江苏苏教版小学英语",
                language = Locale.ENGLISH.toString()
            ),
        )

        books.forEach { book ->
            bookRepository.add(book)
        }

        bookRepository.loadAll()?.forEach { bookEntity ->
            when (bookEntity.name) {
                "江苏人教版小学语文" -> {
                    chGradeDataFactory.setBookId(bookEntity.id)
                    chGradeDataFactory.buildData()
                }

                "江苏苏教版小学英语" -> {
                    enGradeDataFactory.setBookId(bookEntity.id)
                    enGradeDataFactory.buildData()
                }
            }
        }

        logger.d { "buildData finished" }
    }
}