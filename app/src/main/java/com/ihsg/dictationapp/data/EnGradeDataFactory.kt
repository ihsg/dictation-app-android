package com.ihsg.dictationapp.data

import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.repository.GradeRepository
import javax.inject.Inject

class EnGradeDataFactory @Inject constructor(
    private val gradeRepository: GradeRepository,
    private val enGrade5AutumnDataFactory: EnGrade5AutumnDataFactory,
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
                    enGrade5AutumnDataFactory.setBookId(bookId)
                    enGrade5AutumnDataFactory.setGradeId(gradeEntity.id)
                    enGrade5AutumnDataFactory.buildData()
                }
            }
        }

        logger.d { "buildData finished" }
    }
}