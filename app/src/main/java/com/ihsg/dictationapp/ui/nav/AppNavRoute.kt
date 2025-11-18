package com.ihsg.dictationapp.ui.nav

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ihsg.dictationapp.ui.nav.AppNavArgs.ARG_BOOK_ID
import com.ihsg.dictationapp.ui.nav.AppNavArgs.ARG_GRADE_ID

sealed class AppNavRoute(val path: String)

data object HomePageRoute : AppNavRoute("home")

data object BookPageRoute : AppNavRoute("book")
data object AddBookPageRoute : AppNavRoute("add_book")

data object GradePageRoute : AppNavRoute(
    "grade?$ARG_BOOK_ID={$ARG_BOOK_ID}"
) {
    fun getArguments() = listOf(
        navArgument(ARG_BOOK_ID) {
            type = NavType.LongType
            defaultValue = 0L
        }
    )

    fun getBookId(arguments: Bundle?) = arguments?.getLong(ARG_BOOK_ID) ?: 0L
    fun getPathWithArgs(bookId: Long): String = "grade?$ARG_BOOK_ID=$bookId"
}

data object AddGradePageRoute : AppNavRoute(
    "add_grade?$ARG_BOOK_ID={$ARG_BOOK_ID}"
) {
    fun getArguments() = listOf(
        navArgument(ARG_BOOK_ID) {
            type = NavType.LongType
            defaultValue = 0L
        }
    )

    fun getBookId(arguments: Bundle?) = arguments?.getLong(ARG_BOOK_ID) ?: 0L
    fun getPathWithArgs(bookId: Long): String = "add_grade?$ARG_BOOK_ID=$bookId"
}

data object LessonPageRoute : AppNavRoute(
    "lesson?$ARG_BOOK_ID={$ARG_BOOK_ID}&$ARG_GRADE_ID={$ARG_GRADE_ID}"
) {
    fun getArguments() = listOf(
        navArgument(ARG_BOOK_ID) {
            type = NavType.LongType
            defaultValue = 0L
        },
        navArgument(ARG_GRADE_ID) {
            type = NavType.LongType
            defaultValue = 0L
        }
    )

    fun getBookId(arguments: Bundle?) = arguments?.getLong(ARG_BOOK_ID) ?: 0L
    fun getGradeId(arguments: Bundle?) = arguments?.getLong(ARG_GRADE_ID) ?: 0L
    fun getPathWithArgs(bookId: Long, gradeId: Long): String =
        "lesson?$ARG_BOOK_ID=$bookId&$ARG_GRADE_ID=$gradeId"
}

data object AddLessonPageRoute : AppNavRoute(
    "add_lesson?$ARG_BOOK_ID={$ARG_BOOK_ID}&$ARG_GRADE_ID={$ARG_GRADE_ID}"
) {
    fun getArguments() = listOf(
        navArgument(ARG_BOOK_ID) {
            type = NavType.LongType
            defaultValue = 0L
        },
        navArgument(ARG_GRADE_ID) {
            type = NavType.LongType
            defaultValue = 0L
        }
    )

    fun getBookId(arguments: Bundle?) = arguments?.getLong(ARG_BOOK_ID) ?: 0L
    fun getGradeId(arguments: Bundle?) = arguments?.getLong(ARG_GRADE_ID) ?: 0L
    fun getPathWithArgs(bookId: Long, gradeId: Long): String =
        "add_lesson?$ARG_BOOK_ID=$bookId&$ARG_GRADE_ID=$gradeId"
}

data object PlayerPage : AppNavRoute("Player")
data object PlayerSettingsPage : AppNavRoute("PlayerSettings")