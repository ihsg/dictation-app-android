package com.ihsg.dictationapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ihsg.dictationapp.ui.screens.PlayerScreenScreen

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
        }
    }
}