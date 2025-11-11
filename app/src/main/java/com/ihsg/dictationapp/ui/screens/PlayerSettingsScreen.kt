package com.ihsg.dictationapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.vm.HomeVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeVM = hiltViewModel()
) {

    val intervalTime by viewModel.intervalTime.collectAsState()
    var intervalText by remember { mutableStateOf((intervalTime / 1000).toString()) }
    var speechRate by remember { mutableFloatStateOf(1.0f) }
    var pitch by remember { mutableFloatStateOf(1.0f) }

    Scaffold(topBar = {
        TopBar(title = "播放器设置")
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

                // 间隔时间设置
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("间隔时间设置", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("间隔时间:")
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = intervalText,
                                onValueChange = {
                                    intervalText = it.filter { char -> char.isDigit() }
                                    val seconds = intervalText.toIntOrNull() ?: 3
                                    viewModel.setIntervalTime(seconds * 1000L)
                                },
                                modifier = Modifier.width(80.dp),
                                suffix = { Text("秒") }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = (intervalTime / 1000).toFloat(),
                            onValueChange = {
                                val seconds = it.toInt()
                                intervalText = seconds.toString()
                                viewModel.setIntervalTime(seconds * 1000L)
                            },
                            valueRange = 1f..10f,
                            steps = 8
                        )
                    }
                }

                // 语音设置
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("语音设置", style = MaterialTheme.typography.titleMedium)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("语速: ${"%.1f".format(speechRate)}")
                        Slider(
                            value = speechRate,
                            onValueChange = {
                                speechRate = it
                                viewModel.setSpeechRate(it)
                            },
                            valueRange = 0.5f..2.0f,
                            steps = 14
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("音调: ${"%.1f".format(pitch)}")
                        Slider(
                            value = pitch,
                            onValueChange = {
                                pitch = it
                                viewModel.setPitch(it)
                            },
                            valueRange = 0.5f..2.0f,
                            steps = 14
                        )
                    }
                }
            }

        }
    }
}