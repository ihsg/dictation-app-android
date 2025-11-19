package com.ihsg.dictationapp.ext

private val chinesePattern = Regex("[\u4E00-\u9FFF\u3400-\u4DBF\u20000-\u2A6DF]")

fun String.isChinese(): Boolean {
    var chineseCharCount = 0
    var englishCharCount = 0
    var otherCharCount = 0

    this.forEach { char ->
        when (char.code) {
            in 0x4E00..0x9FFF -> chineseCharCount++
            in 0x0041..0x005A, // A-Z
            in 0x0061..0x007A -> englishCharCount++ // a-z
            else -> otherCharCount++
        }
    }

    return chineseCharCount > englishCharCount
}