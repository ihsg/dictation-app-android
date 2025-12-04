package com.ihsg.dictationapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.ihsg.dictationapp.model.player.PlaybackState
import com.ihsg.dictationapp.ui.components.ActionButton
import com.ihsg.dictationapp.ui.components.LargeIcon
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.ui.icon.FilledIcons
import com.ihsg.dictationapp.ui.nav.LocalNavHostController
import com.ihsg.dictationapp.ui.nav.PlayerSettingsPage
import com.ihsg.dictationapp.vm.PlayerVM
import com.ihsg.dictationapp.vm.ValidationVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidationScreen(
    bookId: Long,
    gradeId: Long,
    lessonId: Long,
    modifier: Modifier = Modifier,
    viewModel: ValidationVM = hiltViewModel()
) {
    val navController = LocalNavHostController.current

    val book by viewModel.bookStateFlow.collectAsState()
    val grade by viewModel.gradeStateFlow.collectAsState()
    val lesson by viewModel.lessonStateFlow.collectAsState()
    val lessons by viewModel.lessonsStateFlow.collectAsState()
    val wordList by viewModel.wordList.collectAsState()
    val showLessons by viewModel.showLessonsStateFlow.collectAsState()
    val currentWordIndex by viewModel.currentWordIndex.collectAsState()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.initialize(bookId, gradeId, lessonId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "听写结果校验",
                navigationIcon = { ActionButton(onClick = { navController.popBackStack() }) },
            )
        }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {

            Box(
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${book?.name} > ${grade?.name} > ${lesson?.name}",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Normal,
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
                                text = wordList.getOrNull(currentWordIndex)?.let {
                                    it.word + "\n" + it.tips
                                } ?: "暂无单词",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
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
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }

                if (showLessons) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.weight(1f))

                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .heightIn(min = 100.dp, max = 300.dp)
                                .background(MaterialTheme.colorScheme.onPrimary),
                            contentPadding = PaddingValues(vertical = 10.dp),
                        ) {
                            items(lessons, key = { it.id }) { lesson ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .wrapContentHeight(Alignment.CenterVertically)
                                        .clickable {
                                            viewModel.loadWords(bookId, gradeId, lesson.id)
                                            viewModel.changeLessonsState()
                                        },
                                ) {
                                    Text(
                                        text = lesson.name,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 18.sp,
                                    )
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }

            // playing controller
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // lessons
                IconButton(onClick = { viewModel.changeLessonsState() }) {
                    LargeIcon(
                        imageVector = FilledIcons.Menu,
                        desc = "课程列表"
                    )
                }

                // Previous
                IconButton(
                    onClick = { viewModel.previous() },
                    enabled = wordList.isNotEmpty()
                ) {
                    LargeIcon(
                        imageVector = FilledIcons.SkipPrevious,
                        desc = "上一个"
                    )
                }

                // next
                IconButton(
                    onClick = { viewModel.next() },
                    enabled = wordList.isNotEmpty()
                ) {
                    LargeIcon(
                        imageVector = FilledIcons.SkipNext,
                        desc = "下一个"
                    )
                }

                IconButton(onClick = { viewModel.onWrong() }) {
                    LargeIcon(
                        imageVector = FilledIcons.Cancel,
                        desc = "错误",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                IconButton(onClick = { viewModel.onRight() }) {
                    LargeIcon(
                        imageVector = FilledIcons.CheckCircle,
                        desc = "正确",
                        tint = Color.Green
                    )
                }
            }
        }
    }
}
