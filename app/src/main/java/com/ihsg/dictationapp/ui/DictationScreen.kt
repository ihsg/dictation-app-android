package com.ihsg.dictationapp.ui

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ihsg.dictationapp.DictationViewModel
import com.ihsg.dictationapp.TextToSpeechManager

@Composable
fun DictationScreen(
    viewModel: DictationViewModel = viewModel(
        factory = dictationViewModelFactory(LocalContext.current)
    )
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("english") }

    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val isInitialized by viewModel.isTtsInitialized.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val availableLanguages by viewModel.availableLanguages.collectAsState()

    // 获取实际可用的语言选项
    val availableLanguageOptions = remember(availableLanguages) {
        viewModel.getAvailableLanguageOptions()
    }

    // 语音设置
    var speechRate by remember { mutableStateOf(viewModel.speechRate) }
    var pitch by remember { mutableStateOf(viewModel.pitch) }
    val scrollState = rememberScrollState()

    LaunchedEffect(availableLanguageOptions) {
        if (availableLanguageOptions.isNotEmpty() &&
            !availableLanguageOptions.any { it.second == selectedLanguage }) {
            selectedLanguage = availableLanguageOptions.first().second
        }
    }

    LaunchedEffect(speechRate) {
        viewModel.setSpeechRate(speechRate)
    }

    LaunchedEffect(pitch) {
        viewModel.setPitch(pitch)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(60.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "听写应用",
            style = MaterialTheme.typography.headlineMedium
        )

        // 初始化状态和TTS信息
        if (!isInitialized) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "正在初始化语音引擎...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val intent = viewModel.getInstallTtsDataIntent()
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Done, "下载")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("安装语音数据")
                    }
                }
            }
        } else {
            // 显示可用的语言信息
            if (availableLanguages.isNotEmpty()) {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "支持的语音 (${availableLanguages.size})",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            availableLanguages.joinToString { lg -> lg.displayLanguage },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // 语言选择
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "选择语言",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (availableLanguageOptions.isEmpty()) {
                    Text(
                        "没有可用的语音引擎，请安装语音数据",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableLanguageOptions.forEach { (display, value) ->
                            FilterChip(
                                selected = selectedLanguage == value,
                                onClick = {
                                    selectedLanguage = value
                                    // 测试语音
                                    if (text.isBlank()) {
                                        val testText = when (value) {
                                            "chinese" -> "测试中文语音"
                                            "english" -> "Test English voice"
                                            else -> "Test"
                                        }
                                        viewModel.speakText(testText, value)
                                    }
                                },
                                label = { Text(display) }
                            )
                        }
                    }

                    // 显示语言支持状态
                    if (!isInitialized) {
                        Text(
                            "语音引擎初始化中...",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        Text(
                            "已支持 ${availableLanguages.size} 种语言",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // 文本输入
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "输入文本",
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
                                    "请输入要朗读的文本...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    }
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
                    onValueChange = { speechRate = it },
                    valueRange = 0.5f..2.0f,
                    steps = 14
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("音调: ${"%.1f".format(pitch)}")
                Slider(
                    value = pitch,
                    onValueChange = { pitch = it },
                    valueRange = 0.5f..2.0f,
                    steps = 14
                )
            }
        }

        // 控制按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        viewModel.speakText(text, selectedLanguage)
                    }
                },
                enabled = text.isNotBlank() && isInitialized && !isSpeaking,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSpeaking) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (isSpeaking) Icons.Default.Close else Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isSpeaking) "朗读中..." else "开始朗读")
            }

            if (isSpeaking) {
                Button(
                    onClick = { viewModel.stopSpeaking() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Close, "停止")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("停止")
                }
            }
        }
    }
}

@Composable
fun dictationViewModelFactory(context: Context): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DictationViewModel::class.java)) {
                val ttsManager = TextToSpeechManager(context)
                ttsManager.initialize()
                return DictationViewModel(ttsManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}