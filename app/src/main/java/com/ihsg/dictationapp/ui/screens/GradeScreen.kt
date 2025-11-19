package com.ihsg.dictationapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.ihsg.dictationapp.ui.nav.LessonPageRoute
import com.ihsg.dictationapp.ui.nav.LocalNavHostController
import com.ihsg.dictationapp.vm.GradeVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeScreen(
    bookId: Long,
    modifier: Modifier = Modifier,
    viewModel: GradeVM = hiltViewModel()
) {
    val navController = LocalNavHostController.current

    val book by viewModel.bookStateFlow.collectAsState()
    val grades by viewModel.gradesStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load(bookId)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "年级列表",
                navigationIcon = {
                    ActionButton(onClick = {
                        navController.popBackStack()
                    })
                }
            )
        },
        floatingActionButton = {
            FloatingActionBar(
                onClick = {
                    navController.navigate(AddGradePageRoute.getPathWithArgs(bookId))
                })
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${book?.name}",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Normal,
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
            ) {
                items(grades, key = { it.id }) { grade ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .wrapContentHeight(Alignment.CenterVertically)
                            .clickable {
                                navController.navigate(
                                    LessonPageRoute.getPathWithArgs(
                                        bookId = bookId,
                                        gradeId = grade.id
                                    )
                                )
                            },
                    ) {
                        Text(
                            text = grade.name,
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