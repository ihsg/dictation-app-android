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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ihsg.dictationapp.ui.components.FloatingActionBar
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.ui.nav.AddBookPageRoute
import com.ihsg.dictationapp.ui.nav.GradePageRoute
import com.ihsg.dictationapp.ui.nav.LocalNavHostController
import com.ihsg.dictationapp.vm.BookVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    modifier: Modifier = Modifier,
    viewModel: BookVM = hiltViewModel()
) {
    val navController = LocalNavHostController.current

    val books by viewModel.booksStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        topBar = {
            TopBar(title = "听写小助手")
        },
        floatingActionButton = {
            FloatingActionBar(onClick = { navController.navigate(AddBookPageRoute.path) })
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
            ) {
                items(books, key = { it.id }) { book ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .wrapContentHeight(Alignment.CenterVertically)
                            .clickable {
                                navController.navigate(GradePageRoute.getPathWithArgs(book.id))
                            },
                    ) {
                        Text(
                            text = book.name,
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