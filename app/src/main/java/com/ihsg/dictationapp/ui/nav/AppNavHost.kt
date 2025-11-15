package com.ihsg.dictationapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ihsg.dictationapp.ui.nav.AppNavArgs.ARG_ID
import com.ihsg.dictationapp.ui.screens.AddBookScreen
import com.ihsg.dictationapp.ui.screens.BookScreen
import com.ihsg.dictationapp.ui.screens.GradeScreen
import com.ihsg.dictationapp.ui.screens.PlayerScreen
import com.ihsg.dictationapp.ui.screens.PlayerSettingsScreen

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

            composable(route = PlayerPage.path) {
                PlayerScreen()
            }
            composable(route = PlayerSettingsPage.path) {
                PlayerSettingsScreen()
            }
        }
    }
}