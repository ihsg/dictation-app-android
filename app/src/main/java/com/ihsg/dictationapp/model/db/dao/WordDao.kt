package com.ihsg.dictationapp.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ihsg.dictationapp.model.db.entity.WordEntity

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wordEntity: WordEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(wordEntity: WordEntity)

    @Query("SELECT * from word WHERE book_id = :bookId AND grade_id = :gradeId AND lesson_id = :lessonId")
    suspend fun loadAll(bookId: Long, gradeId: Long, lessonId: Long): List<WordEntity>?
}
