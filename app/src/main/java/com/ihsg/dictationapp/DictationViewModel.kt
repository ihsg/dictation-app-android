package com.ihsg.dictationapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import android.content.Intent
import java.util.Locale

class DictationViewModel(
    private val textToSpeechManager: TextToSpeechManager
) : ViewModel() {

    // TTS 状态
    val isSpeaking = textToSpeechManager.isSpeaking
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    val isTtsInitialized = textToSpeechManager.isInitialized
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    val errorMessage = textToSpeechManager.errorMessage
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    val availableLanguages = textToSpeechManager.availableLanguages
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )

    // 语音设置状态
    private var _speechRate = 1.0f
    val speechRate: Float
        get() = _speechRate

    private var _pitch = 1.0f
    val pitch: Float
        get() = _pitch

    fun speakText(text: String, language: String = "english") {
        when (language.lowercase()) {
            "chinese", "zh" -> textToSpeechManager.speakChinese(text)
            "english", "en" -> textToSpeechManager.speakEnglish(text)
            else -> textToSpeechManager.speak(text)
        }
    }

    fun speakTextWithLanguage(text: String, locale: Locale) {
        textToSpeechManager.speak(text, locale)
    }

    fun setSpeechRate(rate: Float) {
        _speechRate = rate
        textToSpeechManager.setSpeechRate(rate)
    }

    fun setPitch(pitch: Float) {
        _pitch = pitch
        textToSpeechManager.setPitch(pitch)
    }

    fun stopSpeaking() {
        textToSpeechManager.stop()
    }

    fun retryInitialization() {
        // 如果需要重新初始化，可以在这里实现
        // 注意：通常不需要手动重新初始化，因为已经在构造函数中初始化了
    }

    fun clearError() {
        // 清除错误信息 - 可以通过调用一个不产生错误的操作来间接清除
        // 或者我们可以在 TextToSpeechManager 中添加清除错误的方法
    }

    // 获取安装 TTS 数据的 Intent
    fun getInstallTtsDataIntent(): Intent {
        return textToSpeechManager.getInstallTtsDataIntent()
    }

    // 检查当前 TTS 引擎信息
    fun getTtsEngineInfo(): String? {
        return textToSpeechManager.checkTtsEngine()
    }

    fun isLanguageAvailable(languageCode: String): Boolean {
        return when (languageCode.lowercase()) {
            "english", "en" -> isLanguageAvailable(Locale.US) || isLanguageAvailable(Locale.UK)
            "chinese", "zh" -> isLanguageAvailable(Locale.CHINESE) ||
                    isLanguageAvailable(Locale.SIMPLIFIED_CHINESE) ||
                    isLanguageAvailable(Locale.TRADITIONAL_CHINESE)
            else -> true // 默认允许，让 TTS 自己处理
        }
    }

    // 检查特定语言是否可用
    fun isLanguageAvailable(locale: Locale): Boolean {
        return availableLanguages.value.any { availableLocale ->
            availableLocale.language == locale.language
        }
    }

    // 获取实际可用的语言列表
    fun getAvailableLanguageOptions(): List<Pair<String, String>> {
        val options = mutableListOf<Pair<String, String>>()

        if (isLanguageAvailable("chinese")) {
            options.add("中文" to "chinese")
        }
        if (isLanguageAvailable("english")) {
            options.add("英语" to "english")
        }

        // 如果没有可用的语言，至少显示一个默认选项
        if (options.isEmpty()) {
            options.add("默认语言" to "default")
        }

        return options
    }

    // 获取推荐的语言（基于可用性）
    fun getRecommendedLanguage(): Locale {
        return when {
            isLanguageAvailable(Locale.CHINESE) -> Locale.CHINESE
            isLanguageAvailable(Locale.US) -> Locale.US
            isLanguageAvailable(Locale.UK) -> Locale.UK
            availableLanguages.value.isNotEmpty() -> availableLanguages.value.first()
            else -> Locale.getDefault()
        }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeechManager.release()
    }
}