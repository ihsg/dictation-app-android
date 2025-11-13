package com.ihsg.dictationapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ihsg.dictationapp.ui.screens.PlayerScreenScreen
import com.ihsg.dictationapp.ui.screens.PlayerSettingsScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    startRoute: AppNavRoute = HomePage
) {
    CompositionLocalProvider(LocalNavHostController provides rememberNavController()) {
        val navController = LocalNavHostController.current

        NavHost(
            navController = navController,
            startDestination = startRoute.path,
            modifier = modifier
        ) {

            composable(route = HomePage.path) {
                PlayerScreenScreen()
            }
            composable(route = PlayerPage.path) {
                PlayerScreenScreen()
            }
            composable(route = PlayerSettingsPage.path) {
                PlayerSettingsScreen()
            }
        }
    }
}