package com.ihsg.dictationapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.model.config.PageState
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.db.entity.LessonEntity
import com.ihsg.dictationapp.model.db.entity.WordEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.repository.BookRepository
import com.ihsg.dictationapp.model.repository.GradeRepository
import com.ihsg.dictationapp.model.repository.LessonRepository
import com.ihsg.dictationapp.model.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWordVM @Inject constructor(
    private val bookRepository: BookRepository,
    private val gradeRepository: GradeRepository,
    private val lessonRepository: LessonRepository,
    private val wordRepository: WordRepository,
) : ViewModel() {
    private val logger = Logger(this)

    private val _bookStateFlow = MutableStateFlow<BookEntity?>(null)
    val bookStateFlow = _bookStateFlow.asStateFlow()

    private val _gradeStateFlow = MutableStateFlow<GradeEntity?>(null)
    val gradeStateFlow = _gradeStateFlow.asStateFlow()

    private val _lessonStateFlow = MutableStateFlow<LessonEntity?>(null)
    val lessonStateFlow = _lessonStateFlow.asStateFlow()

    private val _pageStateFlow = MutableStateFlow<PageState>(PageState.Initial)
    val pageStateFlow = _pageStateFlow.asStateFlow()

    fun load(bookId: Long, gradeId: Long, lessonId: Long) {
        viewModelScope.launch {
            val bookDeferred = async { bookRepository.loadById(bookId) }
            val gradeDeferred = async { gradeRepository.loadById(bookId, gradeId) }
            val lessonDeferred = async { lessonRepository.loadById(bookId, gradeId, lessonId) }

            _bookStateFlow.value = bookDeferred.await()
            _gradeStateFlow.value = gradeDeferred.await()
            _lessonStateFlow.value = lessonDeferred.await()
        }
    }

    fun add(
        bookId: Long,
        gradeId: Long,
        lessonId: Long,
        word: String,
        tips: String,
        count: String
    ) {
        viewModelScope.launch {
            if (word.isBlank()) {
                logger.e { "the word is null or empty" }
                return@launch
            }

            if (tips.isBlank()) {
                logger.e { "the tips is null or empty" }
                return@launch
            }

            if (count.isBlank()) {
                logger.e { "the count is null or empty" }
                return@launch
            }

            wordRepository.add(
                WordEntity(
                    bookId = bookId,
                    gradeId = gradeId,
                    lessonId = lessonId,
                    word = word,
                    tips = tips,
                    count = count
                )
            )

            _pageStateFlow.value = PageState.Finish
        }
    }
}