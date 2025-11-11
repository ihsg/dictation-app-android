package com.ihsg.dictationapp.ui.components

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ihsg.dictationapp.R
import com.ihsg.dictationapp.ui.icon.FilledIcons
import com.ihsg.dictationapp.ui.theme.DictationAppTheme

@Composable
fun ActionButton(
    imageVector: ImageVector = FilledIcons.ArrowBack,
    descStr: String? = "back",
    descIntRes: Int = R.string.back,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        MediumIcon(
            imageVector = imageVector,
            desc = descStr ?: stringResource(descIntRes),
        )
    }
}

@Preview
@Composable
private fun ActionButtonPreview() {
    DictationAppTheme {
        ActionButton {}
    }
}