package com.ihsg.dictationapp.model.db.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "word")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "book_id")
    val bookId: Long,

    @ColumnInfo(name = "grade_id")
    val gradeId: Long,

    @ColumnInfo(name = "lesson_id")
    val lessonId: Long,

    /**
     * It is real thing that is checked in the dictation:
     * 1. it is the "hanzi" in Chinese, such as "春风化雨".
     * 2. it is the "word" in English, such as "Honey".
     */
    val word: String,

    /**
     * It is the tips for the dictation:
     * 1. it is the "pinyin" in Chinese, such as "chun feng hua yu".
     * 2. it is the "meaningful" in English, such as "亲爱的".
     */
    val tips: String,

    /**
     * It is the count which to indicate the time cost in the dictation:
     * 1. it is the "strokeCount" in Chinese, such as "9+4+4+8=25".
     * 2. it is the "count" of letters of the word in English, such as "5".
     */
    val count: String
)
