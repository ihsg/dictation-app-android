package com.ihsg.dictationapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ihsg.dictationapp.ui.components.ActionButton
import com.ihsg.dictationapp.ui.components.FloatingActionBar
import com.ihsg.dictationapp.ui.components.MediumIcon
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.ui.icon.FilledIcons
import com.ihsg.dictationapp.ui.nav.AddLessonPageRoute
import com.ihsg.dictationapp.ui.nav.AddWordPageRoute
import com.ihsg.dictationapp.ui.nav.LocalNavHostController
import com.ihsg.dictationapp.ui.nav.PlayerPageRoute
import com.ihsg.dictationapp.vm.WordVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen(
    bookId: Long,
    gradeId: Long,
    lessonId: Long,
    modifier: Modifier = Modifier,
    viewModel: WordVM = hiltViewModel()
) {
    val navController = LocalNavHostController.current

    val book by viewModel.bookStateFlow.collectAsState()
    val grade by viewModel.gradeStateFlow.collectAsState()
    val lesson by viewModel.lessonStateFlow.collectAsState()
    val words by viewModel.wordsStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load(bookId, gradeId, lessonId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "词语列表",
                navigationIcon = { ActionButton(onClick = { navController.popBackStack() }) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(
                            PlayerPageRoute.getPathWithArgs(
                                bookId = bookId,
                                gradeId = gradeId,
                                lessonId = lessonId
                            )
                        )
                    }) {
                        MediumIcon(imageVector = FilledIcons.PlayLesson, desc = "play lesson")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionBar(
                onClick = {
                    navController.navigate(
                        AddWordPageRoute.getPathWithArgs(
                            bookId = bookId,
                            gradeId = gradeId,
                            lessonId = lessonId
                        )
                    )
                })
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "${book?.name} > ${grade?.name} > ${lesson?.name}",
                modifier = Modifier.padding(horizontal = 24.dp),
                fontWeight = FontWeight.Bold,
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 24.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
            ) {

                items(words, key = { it.id }) { word ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .wrapContentHeight(Alignment.CenterVertically)
                            .clickable {
                                navController.navigate(
                                    AddLessonPageRoute.getPathWithArgs(
                                        bookId = bookId,
                                        gradeId = gradeId
                                    )
                                )
                            },
                    ) {
                        Text(
                            text = "${word.word} (${word.tips}) (${word.count})",
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