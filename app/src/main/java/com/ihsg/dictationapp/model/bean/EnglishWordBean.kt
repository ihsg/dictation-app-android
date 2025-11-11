package com.ihsg.dictationapp.model.bean

import androidx.annotation.Keep

@Keep
data class EnglishWordBean(
    val word: String,
    val paraphrase: String,
    val letterCount: Int,
)