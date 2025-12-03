package com.ihsg.dictationapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun LargeIcon(
    imageVector: ImageVector,
    desc: String,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = imageVector,
        contentDescription = desc,
        modifier = modifier.size(32.dp),
        tint = MaterialTheme.colorScheme.outline
    )
}
