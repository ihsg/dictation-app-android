package com.ihsg.dictationapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.TextToSpeechManager
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.player.PlaybackManager
import com.ihsg.dictationapp.model.player.PlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeVM @Inject constructor(
    private val playbackManager: PlaybackManager,
) : ViewModel() {
    private val logger = Logger(this)
    val playbackState = playbackManager.playbackState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            PlaybackState.STOPPED
        )

    val currentWordIndex = playbackManager.currentWordIndex
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val wordList = playbackManager.wordList
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val intervalTime = playbackManager.intervalTime
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            3000L
        )

    fun initialize() {
        logger.i { "initialize called" }
        playbackManager.initialize()
    }

    fun setWords(words: List<String>) {
        playbackManager.setWords(words)
    }

    fun play() {
        playbackManager.play()
    }

    fun pause() {
        playbackManager.pause()
    }

    fun stop() {
        playbackManager.stop()
    }

    fun next() {
        playbackManager.next()
    }

    fun previous() {
        playbackManager.previous()
    }

    fun seekTo(index: Int) {
        playbackManager.seekTo(index)
    }

    fun setIntervalTime(intervalMs: Long) {
        playbackManager.setIntervalTime(intervalMs)
    }

    fun setSpeechRate(rate: Float) {
        playbackManager.setSpeechRate(rate)
    }

    fun setPitch(pitch: Float) {
        playbackManager.setPitch(pitch)
    }

    override fun onCleared() {
        super.onCleared()
        playbackManager.release()
    }
}