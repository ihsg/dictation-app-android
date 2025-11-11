package com.ihsg.dictationapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ihsg.dictationapp.ui.theme.DictationAppTheme


@Composable
fun ErrorMessageCard(
    message: String?,
    onClickClose: () -> Unit,
    modifier: Modifier = Modifier,
) {

    if (!message.isNullOrBlank()) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onClickClose) {
                    Icon(Icons.Default.Close, "关闭")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorMessageCardPreview() {
    DictationAppTheme {
        ErrorMessageCard(
            message = "This is the test error message, " +
                    "This is the test error message, " +
                    "This is the test error message, " +
                    "This is the test error message",
            onClickClose = {}
        )
    }
}