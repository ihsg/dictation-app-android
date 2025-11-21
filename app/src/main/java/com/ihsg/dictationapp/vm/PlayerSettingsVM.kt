package com.ihsg.dictationapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.db.entity.LessonEntity
import com.ihsg.dictationapp.model.db.entity.WordEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.player.PlaybackManager
import com.ihsg.dictationapp.model.player.PlaybackState
import com.ihsg.dictationapp.model.repository.BookRepository
import com.ihsg.dictationapp.model.repository.GradeRepository
import com.ihsg.dictationapp.model.repository.LessonRepository
import com.ihsg.dictationapp.model.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerSettingsVM @Inject constructor(
    private val playbackManager: PlaybackManager,
) : ViewModel() {
    private val logger = Logger(this)

    val intervalTime = playbackManager.intervalTime
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            3000L
        )

    val speechRate = playbackManager.speechRate
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            1f
        )

    val pitch = playbackManager.pitch
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            1f
        )

    fun setIntervalTime(intervalMs: Long) {
        playbackManager.setIntervalTime(intervalMs)
    }

    fun setSpeechRate(rate: Float) {
        playbackManager.setSpeechRate(rate)
    }

    fun setPitch(pitch: Float) {
        playbackManager.setPitch(pitch)
    }
}