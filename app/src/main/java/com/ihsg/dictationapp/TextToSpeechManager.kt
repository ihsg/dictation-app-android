package com.ihsg.dictationapp

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.ihsg.dictationapp.model.log.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject

class TextToSpeechManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val logger = Logger(this)

    private var textToSpeech: TextToSpeech? = null

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _availableLanguages = MutableStateFlow<Set<Locale>>(emptySet())
    val availableLanguages: StateFlow<Set<Locale>> = _availableLanguages

    companion object {
        private const val TAG = "TextToSpeechManager"
    }

    fun initialize() {
        logger.d { "Initializing TextToSpeech..." }

        textToSpeech = TextToSpeech(context) { status ->
            logger.d { "TextToSpeech initialization status: $status" }

            when (status) {
                TextToSpeech.SUCCESS -> {
                    logger.d { "TTS initialized successfully" }
                    _isInitialized.value = true
                    _errorMessage.value = null

                    // 检查可用的语言
                    checkAvailableLanguages()

                    // 尝试设置首选语言
                    setPreferredLanguage()
                }

                TextToSpeech.ERROR -> {
                    logger.i { "TTS initialization failed with status: $status" }
                    _isInitialized.value = false
                    _errorMessage.value = "语音引擎初始化失败，请检查设备是否支持TTS功能"
                }

                else -> {
                    logger.i { "TTS initialization unknown status: $status" }
                    _isInitialized.value = false
                    _errorMessage.value = "语音引擎初始化未知错误: $status"
                }
            }
        }

        setupUtteranceListener()
    }

    private fun checkAvailableLanguages() {
        val availableLangs = mutableSetOf<Locale>()
        textToSpeech?.let { tts ->
            try {
                val locales = tts.availableLanguages
                locales?.forEach { locale ->
                    availableLangs.add(locale)
                    logger.d { "Available language: ${locale.displayLanguage} - $locale" }
                }
                _availableLanguages.value = availableLangs
            } catch (e: Exception) {
                logger.d { "Error checking available languages" }
            }
        }
    }

    private fun setPreferredLanguage(): Boolean {
        // 按优先级尝试设置语言
        val preferredLanguages = listOf(
            Locale.CHINESE,
            Locale.US,
            Locale.UK,
            Locale.getDefault()
        )

        for (locale in preferredLanguages) {
            if (setLanguage(locale)) {
                logger.d { "Successfully set language to: ${locale.displayLanguage}" }
                return true
            }
        }

        logger.d { "Failed to set any preferred language" }
        return false
    }

    private fun setupUtteranceListener() {
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                logger.d { "TTS started: $utteranceId" }
                _isSpeaking.value = true
            }

            override fun onDone(utteranceId: String?) {
                logger.d { "TTS completed: $utteranceId" }
                _isSpeaking.value = false
            }

            override fun onError(utteranceId: String?) {
                logger.d { "TTS error: $utteranceId" }
                _isSpeaking.value = false
                _errorMessage.value = "语音播放出错"
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                logger.d { "TTS error: $utteranceId, code: $errorCode" }
                _isSpeaking.value = false
                _errorMessage.value = "语音播放出错: $errorCode"
            }
        })
    }

    fun speak(text: String, language: Locale? = null) {
        if (!_isInitialized.value) {
            logger.d { "TTS not initialized, cannot speak" }
            _errorMessage.value = "语音引擎未初始化"
            return
        }

        if (text.isBlank()) {
            logger.d { "Empty text provided" }
            return
        }

        try {
            // 如果指定了语言，尝试设置
            language?.let { setLanguage(it) }

            val utteranceId = "utterance_${System.currentTimeMillis()}"

            val result = textToSpeech?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                utteranceId
            )

            when (result) {
                TextToSpeech.ERROR -> {
                    logger.d { "Failed to speak text: $text" }
                    _errorMessage.value = "无法播放语音"
                }

                TextToSpeech.SUCCESS -> {
                    logger.d { "Successfully queued speech: $text" }
                    _errorMessage.value = null
                }

                else -> {
                    logger.d { "Unknown speak result: $result" }
                }
            }
        } catch (e: Exception) {
            logger.e { "Exception during speak: ${e.message}" }
            _errorMessage.value = "语音播放异常: ${e.message}"
        }
    }

    fun speakChinese(text: String) {
        speak(text, Locale.CHINESE)
    }

    fun speakEnglish(text: String) {
        speak(text, Locale.US)
    }

    fun setLanguage(locale: Locale): Boolean {
        return try {
            val result = textToSpeech?.setLanguage(locale)
            when (result) {
                TextToSpeech.LANG_COUNTRY_AVAILABLE, TextToSpeech.LANG_AVAILABLE -> {
                    Log.d(TAG, "Language set successfully: ${locale.displayLanguage}")
                    true
                }

                TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE -> {
                    Log.d(TAG, "Language variant available: ${locale.displayLanguage}")
                    true
                }

                TextToSpeech.LANG_MISSING_DATA -> {
                    Log.w(TAG, "Language missing data: ${locale.displayLanguage}")
                    _errorMessage.value = "缺少语音数据，请安装语音包"
                    false
                }

                TextToSpeech.LANG_NOT_SUPPORTED -> {
                    Log.w(TAG, "Language not supported: ${locale.displayLanguage}")
                    _errorMessage.value = "不支持的语言: ${locale.displayLanguage}"
                    false
                }

                else -> {
                    Log.w(
                        TAG,
                        "Unknown language setting result: $result for ${locale.displayLanguage}"
                    )
                    false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting language", e)
            false
        }
    }

    // 检查TTS引擎是否可用
    fun checkTtsEngine(): String? {
        return textToSpeech?.defaultEngine
    }

    // 安装TTS数据的Intent
    fun getInstallTtsDataIntent(): Intent {
        return Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
    }

    fun setSpeechRate(rate: Float) {
        textToSpeech?.setSpeechRate(rate)
    }

    fun setPitch(pitch: Float) {
        textToSpeech?.setPitch(pitch)
    }

    fun stop() {
        try {
            textToSpeech?.stop()
            _isSpeaking.value = false
            Log.d(TAG, "TTS stopped")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping TTS", e)
        }
    }

    fun release() {
        try {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
            textToSpeech = null
            _isInitialized.value = false
            _isSpeaking.value = false
            _errorMessage.value = null
            Log.d(TAG, "TTS released")
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing TTS", e)
        }
    }
}