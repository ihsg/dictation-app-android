package com.ihsg.dictationapp.data

import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.repository.BookRepository
import com.ihsg.dictationapp.model.repository.GradeRepository
import java.util.Locale
import javax.inject.Inject

class ChGradeDataFactory @Inject constructor(
    private val gradeRepository: GradeRepository,
    private val chGrade5AutumnDataFactory: ChGrade5AutumnDataFactory
) : DataFactory, BookSettings {
    private val logger = Logger(this)
    private var bookId: Long = -1L

    override fun setBookId(id: Long) {
        bookId = id
    }

    override suspend fun buildData() {
        logger.d { "buildData called" }

        if (bookId < 0L) {
            logger.e { "buildData called but bookId = $bookId" }
            return
        }

        val gradeNames = listOf(
            "一年级上册",
            "一年级下册",
            "二年级上册",
            "二年级下册",
            "三年级上册",
            "三年级下册",
            "四年级上册",
            "四年级下册",
            "五年级上册",
            "五年级下册",
            "六年级上册",
            "六年级下册"
        )

        gradeNames.forEach { gradeName ->
            val grade = GradeEntity(
                bookId = bookId,
                name = gradeName
            )
            gradeRepository.add(grade)
        }

        gradeRepository.loadAll(bookId)?.forEach { gradeEntity ->
            when (gradeEntity.name) {
                "五年级上册" -> {
                    chGrade5AutumnDataFactory.setBookId(bookId)
                    chGrade5AutumnDataFactory.setGradeId(gradeEntity.id)
                    chGrade5AutumnDataFactory.buildData()
                }
            }
        }

        logger.d { "buildData finished" }
    }
}