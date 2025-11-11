package com.ihsg.dictationapp.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ihsg.dictationapp.ui.nav.LocalNavHostController

@ExperimentalMaterial3Api
@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val navController = LocalNavHostController.current

    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = navigationIcon ?: {
            ActionButton(onClick = { navController.popBackStack() })
        },
        actions = actions
    )
}
