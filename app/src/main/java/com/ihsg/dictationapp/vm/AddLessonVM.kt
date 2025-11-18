package com.ihsg.dictationapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.model.config.PageState
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.db.entity.LessonEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.repository.BookRepository
import com.ihsg.dictationapp.model.repository.GradeRepository
import com.ihsg.dictationapp.model.repository.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddLessonVM @Inject constructor(
    private val bookRepository: BookRepository,
    private val gradeRepository: GradeRepository,
    private val lessonRepository: LessonRepository,
) : ViewModel() {
    private val logger = Logger(this)

    private val _bookStateFlow = MutableStateFlow<BookEntity?>(null)
    val bookStateFlow = _bookStateFlow.asStateFlow()

    private val _gradeStateFlow = MutableStateFlow<GradeEntity?>(null)
    val gradeStateFlow = _gradeStateFlow.asStateFlow()

    private val _pageStateFlow = MutableStateFlow<PageState>(PageState.Initial)
    val pageStateFlow = _pageStateFlow.asStateFlow()

    fun load(bookId: Long, gradeId: Long) {
        viewModelScope.launch {
            _bookStateFlow.value = bookRepository.loadById(bookId)
            _gradeStateFlow.value = gradeRepository.loadById(bookId, gradeId)
        }
    }

    fun add(gradeId: Long, lessonName: String) {
        viewModelScope.launch {
            if (lessonName.isBlank()) {
                logger.e { "the lesson name is null or empty" }
                return@launch
            }

            lessonRepository.add(
                LessonEntity(
                    gradeId = gradeId,
                    name = lessonName
                )
            )

            _pageStateFlow.value = PageState.Finish
        }
    }
}