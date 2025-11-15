package com.ihsg.dictationapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ihsg.dictationapp.ui.components.ActionButton
import com.ihsg.dictationapp.ui.components.FloatingActionBar
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.ui.nav.AddBookPageRoute
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
                title = book?.name.orEmpty(),
                navigationIcon = { ActionButton(onClick = { navController.popBackStack() }) }
            )
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
                items(grades, key = { it.id }) { grade ->
                    Text(
                        text = grade.name,
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .clickable {

                            },
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}