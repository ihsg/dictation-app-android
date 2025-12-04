package com.ihsg.dictationapp.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ihsg.dictationapp.model.db.entity.RecordEntity
import com.ihsg.dictationapp.model.db.entity.WordEntity

@Dao
interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recordEntity: RecordEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(recordEntity: RecordEntity)

    @Query("SELECT * from record WHERE book_id = :bookId AND grade_id = :gradeId AND lesson_id = :lessonId AND word_id = :wordId")
    suspend fun loadByWordId(
        bookId: Long,
        gradeId: Long,
        lessonId: Long,
        wordId: Long
    ): RecordEntity?

    @Query("SELECT * from record WHERE book_id = :bookId AND grade_id = :gradeId AND lesson_id = :lessonId")
    suspend fun loadLessonAll(bookId: Long, gradeId: Long, lessonId: Long): List<RecordEntity>?

    @Query("SELECT * from record WHERE book_id = :bookId AND grade_id = :gradeId")
    suspend fun loadGradeAll(bookId: Long, gradeId: Long): List<RecordEntity>?

    @Query("SELECT * from record WHERE book_id = :bookId")
    suspend fun loadBookAll(bookId: Long): List<RecordEntity>?
}
