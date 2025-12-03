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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerVM @Inject constructor(
    private val playbackManager: PlaybackManager,
    private val bookRepository: BookRepository,
    private val gradeRepository: GradeRepository,
    private val lessonRepository: LessonRepository,
    private val wordRepository: WordRepository,
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

    val wordListFinished = playbackManager.wordListFinished
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

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

    private val _bookStateFlow = MutableStateFlow<BookEntity?>(null)
    val bookStateFlow = _bookStateFlow.asStateFlow()

    private val _gradeStateFlow = MutableStateFlow<GradeEntity?>(null)
    val gradeStateFlow = _gradeStateFlow.asStateFlow()

    private val _lessonsStateFlow = MutableStateFlow<List<LessonEntity>>(emptyList())
    val lessonsStateFlow = _lessonsStateFlow.asStateFlow()

    private val _lessonStateFlow = MutableStateFlow<LessonEntity?>(null)
    val lessonStateFlow = _lessonStateFlow.asStateFlow()

    private val _showLessonsStateFlow = MutableStateFlow(false)
    val showLessonsStateFlow = _showLessonsStateFlow.asStateFlow()

    fun initialize(bookId: Long, gradeId: Long, lessonId: Long) {
        logger.i { "initialize called: bookId=$bookId, gradeId=$gradeId, lessonId=$lessonId" }
        playbackManager.initialize()
        viewModelScope.launch {
            val bookDeferred = async { bookRepository.loadById(bookId) }
            val gradeDeferred = async { gradeRepository.loadById(bookId, gradeId) }
            val lessonsDeferred = async { lessonRepository.loadAll(bookId, gradeId) }
            val lessonDeferred = async { lessonRepository.loadById(bookId, gradeId, lessonId) }
            val wordsDeferred = async { wordRepository.loadAll(bookId, gradeId, lessonId) }

            _bookStateFlow.value = bookDeferred.await()
            _gradeStateFlow.value = gradeDeferred.await()
            _lessonsStateFlow.value = lessonsDeferred.await() ?: emptyList()
            _lessonStateFlow.value = lessonDeferred.await()
            val words = wordsDeferred.await().orEmpty()
            if (words.isNotEmpty()) {
                playbackManager.setWords(words)
            }

            wordListFinished.collectLatest { finished ->
                if (finished) {
                    loadWords(bookId, gradeId, lessonId + 1)
                }
            }
        }
    }

    fun loadWords(bookId: Long, gradeId: Long, lessonId: Long) {
        viewModelScope.launch {
            val lessonDeferred = async { lessonRepository.loadById(bookId, gradeId, lessonId) }
            val wordsDeferred = async { wordRepository.loadAll(bookId, gradeId, lessonId) }
            _lessonStateFlow.value = lessonDeferred.await()
            val words = wordsDeferred.await().orEmpty()
            if (words.isNotEmpty()) {
                playbackManager.setWords(words)
            }
        }
    }

    fun changeLessonsState() {
        _showLessonsStateFlow.value = _showLessonsStateFlow.value.not()
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

    override fun onCleared() {
        super.onCleared()
        playbackManager.release()
    }
}