package com.ihsg.dictationapp.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ihsg.dictationapp.model.db.entity.GradeEntity

@Dao
interface GradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gradeEntity: GradeEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(gradeEntity: GradeEntity)

    @Query("SELECT * FROM grade where bookId = :bookId")
    suspend fun loadAll(bookId: Long): List<GradeEntity>?

    @Query("SELECT * FROM grade where bookId = :bookId AND id = :id")
    suspend fun loadById(bookId: Long, id: Long): GradeEntity?
}
