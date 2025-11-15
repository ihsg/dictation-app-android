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

    @Query("SELECT * FROM lesson WHERE grade_id = :gradeId")
    suspend fun loadAll(gradeId: Long): List<LessonEntity>?

    @Query("SELECT * FROM lesson WHERE grade_id = :gradeId AND id = :id")
    suspend fun loadById(gradeId: Long, id: Long): LessonEntity?
}
