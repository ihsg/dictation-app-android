package com.ihsg.dictationapp.ui.nav

sealed class AppNavRoute(val path: String)

data object HomePage : AppNavRoute("Home")
data object PlayerPage : AppNavRoute("Player")
data object PlayerSettingsPage : AppNavRoute("PlayerSettings")