package com.ihsg.dictationapp.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ihsg.dictationapp.model.db.entity.BookEntity

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookEntity: BookEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(bookEntity: BookEntity)

    @Query("SELECT * FROM book")
    suspend fun loadAll(): List<BookEntity>?

    @Query("SELECT * FROM book WHERE id = :id")
    suspend fun loadById(id: Long): BookEntity?
}
