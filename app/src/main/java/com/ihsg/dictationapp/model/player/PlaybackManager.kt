package com.ihsg.dictationapp.model.player

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.ihsg.dictationapp.model.log.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class PlaybackManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val logger = Logger(this)
    private var textToSpeech: TextToSpeech? = null
    private var job: Job? = null

    private val _playbackState = MutableStateFlow(PlaybackState.STOPPED)
    val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _currentWordIndex = MutableStateFlow(0)
    val currentWordIndex: StateFlow<Int> = _currentWordIndex

    private val _wordList = MutableStateFlow<List<String>>(emptyList())
    val wordList: StateFlow<List<String>> = _wordList

    private val _intervalTime = MutableStateFlow(5000L) // 默认5秒间隔
    val intervalTime: StateFlow<Long> = _intervalTime

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun initialize() {
        textToSpeech = TextToSpeech(context) { status ->
            logger.d { "initialize called: status=$status" }

            if (status == TextToSpeech.SUCCESS) {
                setLanguage(Locale.US)
                setupUtteranceListener()
            }
        }
    }

    private fun setupUtteranceListener() {
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                logger.d { "onStart called: utteranceId=$utteranceId" }
            }

            override fun onDone(utteranceId: String?) {
                logger.d { "onDone called: utteranceId=$utteranceId" }

                if (_playbackState.value == PlaybackState.PLAYING) {
                    scheduleNextWord()
                }
            }

            override fun onError(utteranceId: String?) {
                logger.d { "onError called: utteranceId=$utteranceId" }

                if (_playbackState.value == PlaybackState.PLAYING) {
                    scheduleNextWord()
                }
            }
        })
    }

    private fun setLanguage(locale: Locale): Boolean {
        return textToSpeech?.setLanguage(locale) == TextToSpeech.LANG_AVAILABLE
    }

    fun setWords(words: List<String>) {
        _wordList.value = words
        _currentWordIndex.value = 0
        _playbackState.value = PlaybackState.STOPPED
        job?.cancel()
    }

    fun setIntervalTime(intervalMs: Long) {
        _intervalTime.value = intervalMs
    }

    fun play() {
        if (_wordList.value.isEmpty()) {
            logger.e { "play called but wordList is empty" }
            return
        }

        when (_playbackState.value) {
            PlaybackState.STOPPED -> {
                _playbackState.value = PlaybackState.PLAYING
                speakCurrentWord()
            }

            PlaybackState.PAUSED -> {
                _playbackState.value = PlaybackState.PLAYING
                speakCurrentWord()
            }

            PlaybackState.PLAYING -> {

            }
        }
    }

    fun pause() {
        if (_playbackState.value == PlaybackState.PLAYING) {
            _playbackState.value = PlaybackState.PAUSED
            job?.cancel()
            textToSpeech?.stop()
        }
    }

    fun stop() {
        _playbackState.value = PlaybackState.STOPPED
        _currentWordIndex.value = 0
        job?.cancel()
        textToSpeech?.stop()
    }

    fun next() {
        if (_wordList.value.isEmpty()) return

        job?.cancel()
        textToSpeech?.stop()

        val nextIndex = (_currentWordIndex.value + 1) % _wordList.value.size
        _currentWordIndex.value = nextIndex

        if (_playbackState.value == PlaybackState.PLAYING) {
            speakCurrentWord()
        }
    }

    fun previous() {
        if (_wordList.value.isEmpty()) return

        job?.cancel()
        textToSpeech?.stop()

        val prevIndex = if (_currentWordIndex.value == 0) {
            _wordList.value.size - 1
        } else {
            _currentWordIndex.value - 1
        }
        _currentWordIndex.value = prevIndex

        if (_playbackState.value == PlaybackState.PLAYING) {
            speakCurrentWord()
        }
    }

    fun seekTo(index: Int) {
        if (_wordList.value.isEmpty() || index !in _wordList.value.indices) {
            logger.e { "seekTo called but wordList=$wordList, index=$index, size=${_wordList.value.size}" }
            return
        }

        job?.cancel()
        textToSpeech?.stop()

        _currentWordIndex.value = index

        if (_playbackState.value == PlaybackState.PLAYING) {
            speakCurrentWord()
        }
    }

    private fun speakCurrentWord() {
        val word = _wordList.value.getOrNull(_currentWordIndex.value) ?: return

        val result = textToSpeech?.speak(
            word,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "word_${_currentWordIndex.value}"
        )

        when (result) {
            TextToSpeech.ERROR -> {
                logger.e { "Failed to speak text: $word" }
                _errorMessage.value = "无法播放语音"
            }

            TextToSpeech.SUCCESS -> {
                logger.d { "Successfully queued speech: $word" }
                _errorMessage.value = null
            }

            else -> {
                logger.d { "Unknown speak result: $result" }
            }
        }
    }

    private fun scheduleNextWord() {
        job = CoroutineScope(Dispatchers.Main).launch {
            delay(_intervalTime.value)

            if (_playbackState.value == PlaybackState.PLAYING) {
                val nextIndex = (_currentWordIndex.value + 1) % _wordList.value.size
                _currentWordIndex.value = nextIndex
                speakCurrentWord()
            }
        }
    }

    fun setSpeechRate(rate: Float) {
        textToSpeech?.setSpeechRate(rate)
    }

    fun setPitch(pitch: Float) {
        textToSpeech?.setPitch(pitch)
    }

    fun release() {
        job?.cancel()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}