package com.ihsg.dictationapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ihsg.dictationapp.model.player.PlaybackState
import com.ihsg.dictationapp.ui.components.ActionButton
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.ui.icon.FilledIcons
import com.ihsg.dictationapp.ui.nav.LocalNavHostController
import com.ihsg.dictationapp.ui.nav.PlayerSettingsPage
import com.ihsg.dictationapp.vm.PlayerVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayerVM = hiltViewModel()
) {
    val navController = LocalNavHostController.current

    // 示例单词列表
    val sampleWords = remember {
        listOf(
            "apple", "banana", "computer", "dictionary", "elephant",
            "fantastic", "garden", "happiness", "internet", "journey"
        )
    }

    val playbackState by viewModel.playbackState.collectAsState()
    val currentWordIndex by viewModel.currentWordIndex.collectAsState()
    val wordList by viewModel.wordList.collectAsState()
    val intervalTime by viewModel.intervalTime.collectAsState()
    val speechRate by viewModel.speechRate.collectAsState()
    val pitch by viewModel.pitch.collectAsState()

    LaunchedEffect(Unit) {
        if (wordList.isEmpty()) {
            viewModel.initialize()
            viewModel.setWords(sampleWords)
        }
    }

    Scaffold(topBar = {
        TopBar(title = "播放器",
            actions = {
                ActionButton(imageVector = FilledIcons.Settings) {
                    navController.navigate(PlayerSettingsPage.path)
                }
            })
    }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "第1课 白鹭",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Normal,
                    fontSize = 26.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = wordList.getOrNull(currentWordIndex) ?: "暂无单词",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 60.sp
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 36.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LinearProgressIndicator(
                                modifier = Modifier.weight(1f),
                                progress = { (currentWordIndex + 1f) / wordList.size })

                            Text(
                                text = "${currentWordIndex + 1} / ${wordList.size}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier.padding(top = 30.dp, bottom = 10.dp),
                        text = "当前设置",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "间隔时间: ${intervalTime / 1000L} 秒",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "语音语速: ${"%.1f".format(speechRate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "语音音调: ${"%.1f".format(pitch)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // playing controller
            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous
                    IconButton(
                        onClick = { viewModel.previous() },
                        enabled = wordList.isNotEmpty()
                    ) {
                        Icon(
                            FilledIcons.SkipPrevious,
                            contentDescription = "上一个",
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    // play/pause
                    when (playbackState) {
                        PlaybackState.PLAYING -> {
                            IconButton(
                                onClick = { viewModel.pause() }
                            ) {
                                Icon(
                                    FilledIcons.Pause,
                                    contentDescription = "暂停",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }

                        else -> {
                            IconButton(
                                onClick = { viewModel.play() },
                                enabled = wordList.isNotEmpty()
                            ) {
                                Icon(
                                    FilledIcons.PlayArrow,
                                    contentDescription = "播放",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    }

                    // next
                    IconButton(
                        onClick = { viewModel.next() },
                        enabled = wordList.isNotEmpty()
                    ) {
                        Icon(
                            FilledIcons.SkipNext,
                            contentDescription = "下一个",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }
    }
}
