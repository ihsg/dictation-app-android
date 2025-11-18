package com.ihsg.dictationapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.ui.nav.AddGradePageRoute
import com.ihsg.dictationapp.ui.nav.AddLessonPageRoute
import com.ihsg.dictationapp.ui.nav.LocalNavHostController
import com.ihsg.dictationapp.vm.LessonVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    bookId: Long,
    gradeId: Long,
    modifier: Modifier = Modifier,
    viewModel: LessonVM = hiltViewModel()
) {
    val navController = LocalNavHostController.current

    val book by viewModel.bookStateFlow.collectAsState()
    val grade by viewModel.gradeStateFlow.collectAsState()
    val lessons by viewModel.lessonsStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load(bookId, gradeId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "课程列表",
                navigationIcon = { ActionButton(onClick = { navController.popBackStack() }) }
            )
        },
        floatingActionButton = {
            FloatingActionBar(
                onClick = {
                    navController.navigate(AddLessonPageRoute.getPathWithArgs(bookId, gradeId))
                })
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "${book?.name} > ${grade?.name}",
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            LazyColumn(
                modifier = Modifier.padding(horizontal = 24.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
            ) {

                items(lessons, key = { it.id }) { lesson ->
                    Box(
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