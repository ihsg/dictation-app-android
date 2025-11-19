package com.ihsg.dictationapp.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ihsg.dictationapp.model.config.PageState
import com.ihsg.dictationapp.ui.components.ActionButton
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.ui.nav.LocalNavHostController
import com.ihsg.dictationapp.vm.AddLessonVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLessonScreen(
    bookId: Long,
    gradeId: Long,
    modifier: Modifier = Modifier,
    viewModel: AddLessonVM = hiltViewModel()
) {
    val navController = LocalNavHostController.current
    val book by viewModel.bookStateFlow.collectAsState()
    val grade by viewModel.gradeStateFlow.collectAsState()
    val pageState by viewModel.pageStateFlow.collectAsState()
    var text by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.load(bookId, gradeId)
    }

    LaunchedEffect(pageState) {
        if (pageState == PageState.Finish) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "添加课程",
                navigationIcon = { ActionButton(onClick = { navController.popBackStack() }) }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Text(
                text = "${book?.name} > ${grade?.name}",
                modifier = Modifier.padding(24.dp)
            )

            Card {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        "课程名称",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        textStyle = TextStyle(fontSize = 16.sp),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(16.dp)
                            ) {
                                if (text.isEmpty()) {
                                    Text(
                                        "请输入要添加的课程名称，例如：第1课 白鹭",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(60.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.add(bookId, gradeId, text) }) {
                        Text(text = "添加")
                    }
                }
            }
        }
    }
}