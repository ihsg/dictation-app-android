package com.ihsg.dictationapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ihsg.dictationapp.ui.icon.FilledIcons

@Composable
fun FloatingActionBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        shape = FloatingActionButtonDefaults.largeShape,
        modifier = modifier,
    ) {
        Icon(
            imageVector = FilledIcons.Add,
            contentDescription = "add",
            modifier = modifier.size(24.dp)
        )
    }
}
