package com.ihsg.dictationapp.ui.nav

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

internal val LocalNavHostController = compositionLocalOf<NavHostController> {
    error("No LocalNavHostController provided")
}