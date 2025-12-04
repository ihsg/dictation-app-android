package com.ihsg.dictationapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.db.entity.LessonEntity
import com.ihsg.dictationapp.model.db.entity.RecordEntity
import com.ihsg.dictationapp.model.db.entity.WordEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.repository.BookRepository
import com.ihsg.dictationapp.model.repository.GradeRepository
import com.ihsg.dictationapp.model.repository.LessonRepository
import com.ihsg.dictationapp.model.repository.RecordRepository
import com.ihsg.dictationapp.model.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ValidationVM @Inject constructor(
    private val bookRepository: BookRepository,
    private val gradeRepository: GradeRepository,
    private val lessonRepository: LessonRepository,
    private val wordRepository: WordRepository,
    private val recordRepository: RecordRepository
) : ViewModel() {
    private val logger = Logger(this)

    private val _currentWordIndex = MutableStateFlow(0)
    val currentWordIndex = _currentWordIndex.asStateFlow()

    private val _wordList = MutableStateFlow<List<WordEntity>>(emptyList())
    val wordList = _wordList.asStateFlow()

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
            _wordList.value = wordsDeferred.await().orEmpty()
        }
    }

    fun loadWords(bookId: Long, gradeId: Long, lessonId: Long) {
        viewModelScope.launch {
            val lessonDeferred = async { lessonRepository.loadById(bookId, gradeId, lessonId) }
            val wordsDeferred = async { wordRepository.loadAll(bookId, gradeId, lessonId) }
            _lessonStateFlow.value = lessonDeferred.await()
            _wordList.value = wordsDeferred.await().orEmpty()
        }
    }

    fun changeLessonsState() {
        _showLessonsStateFlow.value = _showLessonsStateFlow.value.not()
    }

    fun next() {
        val nextIndex = (_currentWordIndex.value + 1) % _wordList.value.size
        _currentWordIndex.value = nextIndex
    }

    fun previous() {
        val prevIndex = if (_currentWordIndex.value == 0) {
            _wordList.value.size - 1
        } else {
            _currentWordIndex.value - 1
        }
        _currentWordIndex.value = prevIndex
    }

    fun onWrong() {
        next()
        record(true)
    }

    fun onRight() {
        next()
        record(false)
    }

    private fun record(isError: Boolean) {
        viewModelScope.launch {
            val wordEntity = wordList.value[_currentWordIndex.value]
            val recordEntity = recordRepository.loadByWordId(
                bookId = wordEntity.bookId,
                gradeId = wordEntity.gradeId,
                lessonId = wordEntity.lessonId,
                wordId = wordEntity.id
            )
            val new = if (recordEntity != null) {
                RecordEntity(
                    id = recordEntity.id,
                    bookId = wordEntity.bookId,
                    gradeId = wordEntity.gradeId,
                    lessonId = wordEntity.lessonId,
                    wordId = wordEntity.id,
                    word = wordEntity.word,
                    tips = wordEntity.tips,
                    strokeCount = wordEntity.strokeCount,
                    timesOnWrong = if (isError) recordEntity.timesOnWrong + 1 else recordEntity.timesOnWrong,
                    timesOnRight = if (isError.not()) recordEntity.timesOnRight else recordEntity.timesOnRight + 1
                )
            } else {
                RecordEntity(
                    bookId = wordEntity.bookId,
                    gradeId = wordEntity.gradeId,
                    lessonId = wordEntity.lessonId,
                    wordId = wordEntity.id,
                    word = wordEntity.word,
                    tips = wordEntity.tips,
                    strokeCount = wordEntity.strokeCount,
                    timesOnWrong = if (isError) 1 else 0,
                    timesOnRight = if (isError.not()) 1 else 0
                )
            }

            recordRepository.update(new)
        }
    }
}