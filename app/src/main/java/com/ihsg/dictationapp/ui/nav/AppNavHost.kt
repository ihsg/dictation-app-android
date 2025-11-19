package com.ihsg.dictationapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ihsg.dictationapp.ui.screens.AddBookScreen
import com.ihsg.dictationapp.ui.screens.AddGradeScreen
import com.ihsg.dictationapp.ui.screens.AddLessonScreen
import com.ihsg.dictationapp.ui.screens.BookScreen
import com.ihsg.dictationapp.ui.screens.GradeScreen
import com.ihsg.dictationapp.ui.screens.LessonScreen
import com.ihsg.dictationapp.ui.screens.PlayerScreen
import com.ihsg.dictationapp.ui.screens.PlayerSettingsScreen
import com.ihsg.dictationapp.ui.screens.WordScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    startRoute: AppNavRoute = HomePageRoute
) {
    CompositionLocalProvider(LocalNavHostController provides rememberNavController()) {
        val navController = LocalNavHostController.current

        NavHost(
            navController = navController,
            startDestination = startRoute.path,
            modifier = modifier
        ) {

            composable(route = HomePageRoute.path) {
                BookScreen()
            }
            composable(route = BookPageRoute.path) {
                BookScreen()
            }
            composable(route = AddBookPageRoute.path) {
                AddBookScreen()
            }

            composable(
                route = GradePageRoute.path,
                arguments = GradePageRoute.getArguments()
            ) {
                GradeScreen(bookId = GradePageRoute.getBookId(it.arguments))
            }

            composable(
                route = AddGradePageRoute.path,
                arguments = AddGradePageRoute.getArguments()
            ) {
                AddGradeScreen(bookId = AddGradePageRoute.getBookId(it.arguments))
            }

            composable(
                route = LessonPageRoute.path,
                arguments = LessonPageRoute.getArguments()
            ) {
                LessonScreen(
                    bookId = LessonPageRoute.getBookId(it.arguments),
                    gradeId = LessonPageRoute.getGradeId(it.arguments)
                )
            }

            composable(
                route = AddLessonPageRoute.path,
                arguments = AddLessonPageRoute.getArguments()
            ) {
                AddLessonScreen(
                    bookId = AddLessonPageRoute.getBookId(it.arguments),
                    gradeId = AddLessonPageRoute.getGradeId(it.arguments)
                )
            }

            composable(
                route = WordPageRoute.path,
                arguments = WordPageRoute.getArguments()
            ) {
                WordScreen(
                    bookId = WordPageRoute.getBookId(it.arguments),
                    gradeId = WordPageRoute.getGradeId(it.arguments),
                    lessonId = WordPageRoute.getLessonId(it.arguments)
                )
            }

            composable(
                route = AddWordPageRoute.path,
                arguments = AddWordPageRoute.getArguments()
            ) {
                AddLessonScreen(
                    bookId = AddWordPageRoute.getBookId(it.arguments),
                    gradeId = AddWordPageRoute.getGradeId(it.arguments),
                    lessonId = AddWordPageRoute.getLessonId(it.arguments)
                )
            }

            composable(route = PlayerPage.path) {
                PlayerScreen()
            }
            composable(route = PlayerSettingsPage.path) {
                PlayerSettingsScreen()
            }
        }
    }
}