package com.ihsg.dictationapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsg.dictationapp.model.config.PageState
import com.ihsg.dictationapp.model.db.entity.BookEntity
import com.ihsg.dictationapp.model.db.entity.GradeEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.repository.BookRepository
import com.ihsg.dictationapp.model.repository.GradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGradeVM @Inject constructor(
    private val bookRepository: BookRepository,
    private val gradeRepository: GradeRepository,
) : ViewModel() {
    private val logger = Logger(this)

    private val _bookStateFlow = MutableStateFlow<BookEntity?>(null)
    val bookStateFlow = _bookStateFlow.asStateFlow()

    private val _pageStateFlow = MutableStateFlow<PageState>(PageState.Initial)
    val pageStateFlow = _pageStateFlow.asStateFlow()

    fun load(bookId: Long) {
        viewModelScope.launch {
            _bookStateFlow.value = bookRepository.loadById(bookId)
        }
    }

    fun add(bookId: Long, gradeName: String) {
        viewModelScope.launch {
            if (gradeName.isBlank()) {
                logger.e { "the grade name is null or empty" }
                return@launch
            }

            gradeRepository.add(
                GradeEntity(
                    bookId = bookId,
                    name = gradeName
                )
            )

            _pageStateFlow.value = PageState.Finish
        }
    }
}