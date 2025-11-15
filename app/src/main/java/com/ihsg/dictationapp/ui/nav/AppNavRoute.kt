package com.ihsg.dictationapp.ui.nav

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ihsg.dictationapp.ui.nav.AppNavArgs.ARG_ID

sealed class AppNavRoute(val path: String)

data object HomePageRoute : AppNavRoute("home")

data object BookPageRoute : AppNavRoute("book")
data object AddBookPageRoute : AppNavRoute("add_book")

data object GradePageRoute : AppNavRoute("grade?$ARG_ID={$ARG_ID}") {

    fun getArguments() = listOf(
        navArgument(ARG_ID) {
            type = NavType.LongType
            defaultValue = 0L
        }
    )

    fun getBookId(arguments: Bundle?) = arguments?.getLong(ARG_ID) ?: 0L

    fun getPathWithArgs(bookId: Long): String = "grade?$ARG_ID=$bookId"

}

data object AddGradePageRoute : AppNavRoute("add_grade?$ARG_ID={$ARG_ID}") {
    fun getArguments() = listOf(
        navArgument(ARG_ID) {
            type = NavType.LongType
            defaultValue = 0L
        }
    )

    fun getBookId(arguments: Bundle?) = arguments?.getLong(ARG_ID) ?: 0L

    fun getPathWithArgs(bookId: Long): String = "add_grade?$ARG_ID=$bookId"
}


data object PlayerPage : AppNavRoute("Player")
data object PlayerSettingsPage : AppNavRoute("PlayerSettings")