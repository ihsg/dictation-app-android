package com.ihsg.dictationapp.model.db.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "grade")
data class GradeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "bookId")
    val bookId: Long,

    @ColumnInfo(name = "name")
    val name: String,
)
