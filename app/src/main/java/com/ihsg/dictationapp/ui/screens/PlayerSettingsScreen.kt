package com.ihsg.dictationapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ihsg.dictationapp.ui.components.ActionButton
import com.ihsg.dictationapp.ui.components.TopBar
import com.ihsg.dictationapp.ui.nav.LocalNavHostController
import com.ihsg.dictationapp.vm.PlayerSettingsVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayerSettingsVM = hiltViewModel()
) {
    val navController = LocalNavHostController.current

    val intervalTime by viewModel.intervalTime.collectAsState()
    val speechRate by viewModel.speechRate.collectAsState()
    val pitch by viewModel.pitch.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                title = "播放器设置",
                navigationIcon = {
                    ActionButton(onClick = { navController.popBackStack() })
                })
        }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.onPrimary)
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
                        Text(
                            text = "间隔时间设置",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("间隔时间: ${intervalTime / 1000L} 秒")

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = (intervalTime / 1000).toFloat(),
                            onValueChange = {
                                val seconds = it.toInt()
                                viewModel.setIntervalTime(seconds * 1000L)
                            },
                            valueRange = 1f..20f,
                            steps = 19
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