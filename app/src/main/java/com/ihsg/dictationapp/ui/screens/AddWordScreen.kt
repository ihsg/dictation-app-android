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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ihsg.dictationapp.model.config.PageState
import com.ihsg.dictationapp.ui.components.ActionButton
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.ui.nav.LocalNavHostController
import com.ihsg.dictationapp.vm.AddWordVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLessonScreen(
    bookId: Long,
    gradeId: Long,
    lessonId: Long,
    modifier: Modifier = Modifier,
    viewModel: AddWordVM = hiltViewModel()
) {
    val navController = LocalNavHostController.current

    val book by viewModel.bookStateFlow.collectAsState()
    val grade by viewModel.gradeStateFlow.collectAsState()
    val lesson by viewModel.lessonStateFlow.collectAsState()

    val pageState by viewModel.pageStateFlow.collectAsState()
    var text by remember { mutableStateOf("") }
    var tips by remember { mutableStateOf("") }
    var count by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.load(bookId, gradeId, lessonId)
    }

    LaunchedEffect(pageState) {
        if (pageState == PageState.Finish) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "添加词语",
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
                text = "${book?.name} > ${grade?.name} > ${lesson?.name}",
                modifier = Modifier.padding(horizontal = 24.dp),
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        "词语",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
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
                                        "请输入要添加的词语或单词，例如：汉语'精巧', 英语'Hello'",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "听写提示",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = tips,
                        onValueChange = { tips = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
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
                                if (tips.isEmpty()) {
                                    Text(
                                        "请输入要添加的词语或单词的听写提示，例如：汉语词语对应的拼音'jing qiao', 英语单词对应的汉语意思'你好'",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "书写笔划数",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = count,
                        onValueChange = { count = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
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
                                if (count.isEmpty()) {
                                    Text(
                                        "请输入要添加的词语或单词的总的书写笔划数，例如：汉字词语中每个汉字的笔划数之和'19', 英语单词中字母数之和'5'",
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
                        onClick = { viewModel.add(bookId, gradeId, lessonId, text, tips, count) }) {
                        Text(text = "添加")
                    }
                }
            }
        }
    }
}