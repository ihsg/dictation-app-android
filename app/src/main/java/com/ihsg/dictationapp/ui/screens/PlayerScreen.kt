package com.ihsg.dictationapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ihsg.dictationapp.model.player.PlaybackState
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.ui.icon.FilledIcons
import com.ihsg.dictationapp.ui.theme.DictationAppTheme
import com.ihsg.dictationapp.vm.HomeVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreenScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeVM = hiltViewModel()
) {
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

    LaunchedEffect(Unit) {
        if (wordList.isEmpty()) {
            viewModel.initialize()
            viewModel.setWords(sampleWords)
        }
    }

    Scaffold(topBar = {
        TopBar(title = "英语单词播放器")
    }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    CircularProgressIndicator(progress = { (currentWordIndex + 1f) / wordList.size })
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "当前单词",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = wordList.getOrNull(currentWordIndex) ?: "暂无单词",
                            style = MaterialTheme.typography.displayMedium,
                            fontSize = 40.sp
                        )
                        Text(
                            text = "${currentWordIndex + 1} / ${wordList.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 单词列表
                if (wordList.isNotEmpty()) {
                    Card {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("单词列表", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3), // 每行3个单词
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.height(200.dp) // 限制高度，内部可滚动
                            ) {
                                itemsIndexed(wordList) { index, word ->
                                    val isCurrent = index == currentWordIndex

                                    Card(
                                        onClick = { viewModel.seekTo(index) },
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isCurrent)
                                                MaterialTheme.colorScheme.primaryContainer
                                            else
                                                MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                        modifier = Modifier
                                            .height(60.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = word,
                                                style = MaterialTheme.typography.bodyMedium,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
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
                            modifier = Modifier.size(32.dp)
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
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PlayerScreenScreenPreview() {
    DictationAppTheme {
        PlayerScreenScreen()
    }
}