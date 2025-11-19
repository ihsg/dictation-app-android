package com.ihsg.dictationapp.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ihsg.dictationapp.model.db.entity.LessonEntity

@Dao
interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lessonEntity: LessonEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(lessonEntity: LessonEntity)

    @Query("SELECT * FROM lesson WHERE book_id = :bookId AND grade_id = :gradeId")
    suspend fun loadAll(bookId:Long, gradeId: Long): List<LessonEntity>?

    @Query("SELECT * FROM lesson WHERE book_id = :bookId AND grade_id = :gradeId AND id = :lessonId")
    suspend fun loadById(bookId:Long, gradeId: Long, lessonId: Long): LessonEntity?
}
